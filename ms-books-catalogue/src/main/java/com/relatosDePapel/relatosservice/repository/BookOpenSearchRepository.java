package com.relatosDePapel.relatosservice.repository;

import com.relatosDePapel.relatosservice.document.BookDocument;
import com.relatosDePapel.relatosservice.dto.BookSearchParamsDTO;
import org.opensearch.client.json.JsonData;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.FieldValue;
import org.opensearch.client.opensearch._types.SortOrder;
import org.opensearch.client.opensearch._types.aggregations.Aggregation;
import org.opensearch.client.opensearch._types.query_dsl.*;
import org.opensearch.client.opensearch.core.*;
import org.opensearch.client.opensearch.core.search.Hit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class BookOpenSearchRepository {

    private final OpenSearchClient client;
    private final String index;

    public BookOpenSearchRepository(OpenSearchClient client,
                                    @Value("${opensearch.index:books}") String index) {
        this.client = client;
        this.index = index;
    }

    // ─── Siguiente ID autoincremental ────────────────────────────────────────────

    public Long nextId() throws IOException {
        var response = client.search(s -> s
                        .index(index)
                        .size(1)
                        .sort(so -> so.field(f -> f.field("id").order(SortOrder.Desc)))
                        .query(q -> q.matchAll(m -> m)),
                BookDocument.class);

        List<Hit<BookDocument>> hits = response.hits().hits();
        if (hits.isEmpty()) return 1L;
        BookDocument last = hits.get(0).source();
        return last != null && last.getId() != null ? last.getId() + 1 : 1L;
    }

    // ─── Guardar / actualizar ────────────────────────────────────────────────────

    public BookDocument save(BookDocument doc) throws IOException {
        client.index(i -> i
                .index(index)
                .id(String.valueOf(doc.getId()))
                .document(doc));
        client.indices().refresh(r -> r.index(index));
        return doc;
    }

    // ─── Buscar por ID ──────────────────────────────────────────────────────────

    public Optional<BookDocument> findById(Long id) throws IOException {
        GetResponse<BookDocument> response = client.get(g -> g
                        .index(index)
                        .id(String.valueOf(id)),
                BookDocument.class);
        if (!response.found() || response.source() == null) return Optional.empty();
        return Optional.of(response.source());
    }

    // ─── Comprobar ISBN ──────────────────────────────────────────────────────────

    public boolean existsByIsbn(String isbn) throws IOException {
        var response = client.search(s -> s
                        .index(index)
                        .size(1)
                        .query(q -> q.term(t -> t.field("isbn").value(FieldValue.of(isbn)))),
                BookDocument.class);
        return response.hits().total() != null && response.hits().total().value() > 0;
    }

    public boolean existsByIsbnAndIdNot(String isbn, Long id) throws IOException {
        var response = client.search(s -> s
                        .index(index)
                        .size(1)
                        .query(q -> q.bool(b -> b
                                .must(m -> m.term(t -> t.field("isbn").value(FieldValue.of(isbn))))
                                .mustNot(mn -> mn.term(t -> t.field("id").value(FieldValue.of(id))))
                        )),
                BookDocument.class);
        return response.hits().total() != null && response.hits().total().value() > 0;
    }

    // ─── Comprobar existencia por ID ─────────────────────────────────────────────

    public boolean existsById(Long id) throws IOException {
        return client.exists(e -> e.index(index).id(String.valueOf(id))).value();
    }

    // ─── Todos los visibles (paginado) ──────────────────────────────────────────

    public PageResult findAllVisible(int page, int size) throws IOException {
        int from = page * size;
        var response = client.search(s -> s
                        .index(index)
                        .from(from)
                        .size(size)
                        .query(q -> q.term(t -> t.field("visible").value(FieldValue.of(true)))),
                BookDocument.class);
        long total = response.hits().total() != null ? response.hits().total().value() : 0;
        return new PageResult(extractHits(response), total);
    }

    // ─── Todos los visibles sin paginar (para facets) ───────────────────────────

    public List<BookDocument> findAllVisibleForFacets() throws IOException {
        var response = client.search(s -> s
                        .index(index)
                        .size(10000)
                        .query(q -> q.term(t -> t.field("visible").value(FieldValue.of(true)))),
                BookDocument.class);
        return extractHits(response);
    }

    // ─── Búsqueda con filtros (paginada) ────────────────────────────────────────

    public PageResult search(BookSearchParamsDTO params) throws IOException {
        List<Query> filters = buildFilters(params);
        int from = params.getPage() * params.getSize();
        int size = params.getSize();

        var response = client.search(s -> {
            var builder = s.index(index).from(from).size(size);
            if (filters.isEmpty()) {
                builder.query(q -> q.matchAll(m -> m));
            } else {
                builder.query(q -> q.bool(b -> b.filter(filters)));
            }
            return builder;
        }, BookDocument.class);

        long total = response.hits().total() != null ? response.hits().total().value() : 0;
        return new PageResult(extractHits(response), total);
    }

    // ─── Búsqueda sin paginar (para facets) ─────────────────────────────────────

    public List<BookDocument> searchForFacets(BookSearchParamsDTO params) throws IOException {
        List<Query> filters = buildFilters(params);
        var response = client.search(s -> {
            var builder = s.index(index).size(10000);
            if (filters.isEmpty()) {
                builder.query(q -> q.matchAll(m -> m));
            } else {
                builder.query(q -> q.bool(b -> b.filter(filters)));
            }
            return builder;
        }, BookDocument.class);
        return extractHits(response);
    }

    // ─── Búsqueda multi-match con fuzziness (paginada) ──────────────────────────

    public PageResult searchByText(String text, BookSearchParamsDTO params) throws IOException {
        List<Query> filters = buildFilters(params);
        int from = params.getPage() * params.getSize();
        int size = params.getSize();

        Query textQuery = Query.of(q -> q.multiMatch(m -> m
                .query(text)
                .fields(List.of("title", "title.search", "author", "author.search"))
                .fuzziness("1")
                .type(TextQueryType.BoolPrefix)
        ));

        var response = client.search(s -> s
                        .index(index)
                        .from(from)
                        .size(size)
                        .query(q -> q.bool(b -> b
                                .must(textQuery)
                                .filter(filters)
                        )),
                BookDocument.class);

        long total = response.hits().total() != null ? response.hits().total().value() : 0;
        return new PageResult(extractHits(response), total);
    }

    // ─── Búsqueda por texto sin paginar (para facets) ───────────────────────────

    public List<BookDocument> searchByTextForFacets(String text, BookSearchParamsDTO params) throws IOException {
        List<Query> filters = buildFilters(params);

        Query textQuery = Query.of(q -> q.multiMatch(m -> m
                .query(text)
                .fields(List.of("title", "title.search", "author", "author.search"))
                .fuzziness("1")
                .type(TextQueryType.BoolPrefix)
        ));

        var response = client.search(s -> s
                        .index(index)
                        .size(10000)
                        .query(q -> q.bool(b -> b
                                .must(textQuery)
                                .filter(filters)
                        )),
                BookDocument.class);
        return extractHits(response);
    }

    // ─── Aggregaciones para facets ───────────────────────────────────────────────

    public SearchResponse<BookDocument> searchWithAggregations() throws IOException {
        return client.search(s -> s
                        .index(index)
                        .size(0)
                        .query(q -> q.term(t -> t.field("visible").value(FieldValue.of(true))))
                        .aggregations(Map.of(
                                "by_category", Aggregation.of(a -> a
                                        .terms(t -> t.field("category").size(50))),
                                "by_rating", Aggregation.of(a -> a
                                        .terms(t -> t.field("rating").size(10))),
                                "price_range", Aggregation.of(a -> a
                                        .stats(st -> st.field("price"))),
                                "with_stock", Aggregation.of(a -> a
                                        .filter(f -> f.range(r -> r
                                                .field("stock").gt(JsonData.of(0)))))
                        )),
                BookDocument.class);
    }

    // ─── Soft delete ─────────────────────────────────────────────────────────────

    public void deleteById(Long id) throws IOException {
        client.delete(d -> d.index(index).id(String.valueOf(id)));
        client.indices().refresh(r -> r.index(index));
    }

    // ─── Helper: construir filtros ────────────────────────────────────────────────

    private List<Query> buildFilters(BookSearchParamsDTO params) {
        List<Query> filters = new ArrayList<>();

        if (params.getVisible() != null) {
            filters.add(Query.of(q -> q.term(t -> t.field("visible")
                    .value(FieldValue.of(params.getVisible())))));
        }

        if (params.getCategory() != null && !params.getCategory().isBlank()) {
            filters.add(Query.of(q -> q.term(t -> t.field("category")
                    .value(FieldValue.of(params.getCategory())))));
        }

        if (params.getIsbn() != null && !params.getIsbn().isBlank()) {
            filters.add(Query.of(q -> q.term(t -> t.field("isbn")
                    .value(FieldValue.of(params.getIsbn())))));
        }

        if (params.getRating() != null) {
            filters.add(Query.of(q -> q.term(t -> t.field("rating")
                    .value(FieldValue.of(params.getRating())))));
        }

        if (Boolean.TRUE.equals(params.getInStock())) {
            filters.add(Query.of(q -> q.range(r -> r
                    .field("stock").gt(JsonData.of(0)))));
        } else if (Boolean.FALSE.equals(params.getInStock())) {
            filters.add(Query.of(q -> q.term(t -> t.field("stock")
                    .value(FieldValue.of(0)))));
        }

        if (params.getMinPrice() != null || params.getMaxPrice() != null) {
            final Float min = params.getMinPrice();
            final Float max = params.getMaxPrice();
            filters.add(Query.of(q -> q.range(r -> {
                var b = r.field("price");
                if (min != null) b = b.gte(JsonData.of(min));
                if (max != null) b = b.lte(JsonData.of(max));
                return b;
            })));
        }

        return filters;
    }

    // ─── Helper: extraer hits ─────────────────────────────────────────────────────

    private List<BookDocument> extractHits(SearchResponse<BookDocument> response) {
        List<BookDocument> result = new ArrayList<>();
        for (Hit<BookDocument> hit : response.hits().hits()) {
            if (hit.source() != null) result.add(hit.source());
        }
        return result;
    }

    // ─── Resultado paginado ───────────────────────────────────────────────────────

    public static class PageResult {
        private final List<BookDocument> docs;
        private final long total;

        public PageResult(List<BookDocument> docs, long total) {
            this.docs = docs;
            this.total = total;
        }

        public List<BookDocument> getDocs() { return docs; }
        public long getTotal() { return total; }
    }
}
