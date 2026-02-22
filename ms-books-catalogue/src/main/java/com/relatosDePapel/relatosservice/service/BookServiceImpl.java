package com.relatosDePapel.relatosservice.service;

import com.relatosDePapel.relatosservice.document.BookDocument;
import com.relatosDePapel.relatosservice.dto.*;
import com.relatosDePapel.relatosservice.exception.BookNotFoundException;
import com.relatosDePapel.relatosservice.exception.DuplicateIsbnException;
import com.relatosDePapel.relatosservice.mapper.BookMapper;
import com.relatosDePapel.relatosservice.repository.BookOpenSearchRepository;
import org.opensearch.client.opensearch._types.aggregations.StringTermsBucket;
import org.opensearch.client.opensearch._types.aggregations.LongTermsBucket;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementación del servicio para la gestión de libros
 */
@Service
public class BookServiceImpl implements BookService {

    private final BookOpenSearchRepository repository;
    private final BookMapper bookMapper;

    public BookServiceImpl(BookOpenSearchRepository repository, BookMapper bookMapper) {
        this.repository = repository;
        this.bookMapper = bookMapper;
    }

    /**
     * Listar todos los libros visibles paginados + facets sobre el total
     */
    @Override
    public BookSearchResponseDTO getAllBooks() {
        try {
            BookSearchParamsDTO params = new BookSearchParamsDTO();
            params.setPage(0);
            params.setSize(10);
            return searchBooks(params);
        } catch (Exception e) {
            throw new RuntimeException("Error al consultar OpenSearch", e);
        }
    }

    /**
     * Buscar libro por ID
     */
    @Override
    public BookResponseDTO getBookById(Long id) {
        try {
            BookDocument doc = repository.findById(id)
                    .orElseThrow(() -> new BookNotFoundException(id));
            return bookMapper.toResponseDTO(doc);
        } catch (IOException e) {
            throw new RuntimeException("Error al consultar OpenSearch", e);
        }
    }

    /**
     * Crear un nuevo libro
     */
    @Override
    public BookResponseDTO createBook(BookCreateRequestDTO dto) {
        try {
            if (repository.existsByIsbn(dto.getIsbn())) {
                throw new DuplicateIsbnException(dto.getIsbn());
            }
            Long newId = repository.nextId();
            BookDocument doc = bookMapper.toDocument(dto, newId);
            BookDocument saved = repository.save(doc);
            return bookMapper.toResponseDTO(saved);
        } catch (DuplicateIsbnException e) {
            throw e;
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar en OpenSearch", e);
        }
    }

    /**
     * Actualizar un libro completo (PUT)
     */
    @Override
    public BookResponseDTO updateBook(Long id, BookUpdateRequestDTO dto) {
        try {
            if (!repository.existsById(id)) {
                throw new BookNotFoundException(id);
            }
            if (repository.existsByIsbnAndIdNot(dto.getIsbn(), id)) {
                throw new DuplicateIsbnException(dto.getIsbn());
            }
            BookDocument doc = bookMapper.toDocument(dto, id);
            BookDocument saved = repository.save(doc);
            return bookMapper.toResponseDTO(saved);
        } catch (BookNotFoundException | DuplicateIsbnException e) {
            throw e;
        } catch (IOException e) {
            throw new RuntimeException("Error al actualizar en OpenSearch", e);
        }
    }

    /**
     * Actualizar un libro parcialmente (PATCH)
     */
    @Override
    public BookResponseDTO patchBook(Long id, BookPatchRequestDTO dto) {
        try {
            BookDocument doc = repository.findById(id)
                    .orElseThrow(() -> new BookNotFoundException(id));

            if (dto.getIsbn() != null && !dto.getIsbn().equals(doc.getIsbn())) {
                if (repository.existsByIsbn(dto.getIsbn())) {
                    throw new DuplicateIsbnException(dto.getIsbn());
                }
            }
            bookMapper.updateFromPatchDTO(doc, dto);
            BookDocument saved = repository.save(doc);
            return bookMapper.toResponseDTO(saved);
        } catch (BookNotFoundException | DuplicateIsbnException e) {
            throw e;
        } catch (IOException e) {
            throw new RuntimeException("Error al actualizar en OpenSearch", e);
        }
    }

    /**
     * Eliminar un libro (soft delete - cambia visible a false)
     */
    @Override
    public void deleteBook(Long id) {
        try {
            BookDocument doc = repository.findById(id)
                    .orElseThrow(() -> new BookNotFoundException(id));
            // Soft delete: visible = false
            doc.setVisible(false);
            repository.save(doc);
        } catch (BookNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar en OpenSearch", e);
        }
    }

    /**
     * Buscar libros con filtros — devuelve la página solicitada + facets sobre el total
     */
    @Override
    public BookSearchResponseDTO searchBooks(BookSearchParamsDTO params) {
        try {
            boolean hasText = (params.getTitle() != null && !params.getTitle().isBlank())
                    || (params.getAuthor() != null && !params.getAuthor().isBlank());

            // 1. Resultado paginado (solo los N libros de la página)
            BookOpenSearchRepository.PageResult pageResult;
            // 2. Total sin paginar (para facets y totalElements)
            List<BookDocument> allDocs;

            if (hasText) {
                String text = buildTextQuery(params);
                pageResult = repository.searchByText(text, params);
                allDocs = repository.searchByTextForFacets(text, params);
            } else {
                pageResult = repository.search(params);
                allDocs = repository.searchForFacets(params);
            }

            List<BookResponseDTO> books = pageResult.getDocs().stream()
                    .map(bookMapper::toResponseDTO)
                    .collect(Collectors.toList());

            // Facets calculados sobre TODOS los resultados, no solo la página
            BookFacetsDTO facets = buildFacetsFromDocs(allDocs);

            return new BookSearchResponseDTO(books, facets,
                    pageResult.getTotal(), params.getPage(), params.getSize());
        } catch (IOException e) {
            throw new RuntimeException("Error al buscar en OpenSearch", e);
        }
    }

    @Override
    public BookFacetsDTO getFacets() {
        try {
            SearchResponse<BookDocument> response = repository.searchWithAggregations();
            BookFacetsDTO facets = new BookFacetsDTO();

            // Categorías
            var categoryAgg = response.aggregations().get("by_category");
            if (categoryAgg != null) {
                List<BookFacetsDTO.FacetBucket> categories = new ArrayList<>();
                for (StringTermsBucket bucket : categoryAgg.sterms().buckets().array()) {
                    categories.add(new BookFacetsDTO.FacetBucket(
                            bucket.key(),
                            bucket.docCount()
                    ));
                }
                facets.setCategories(categories);
            }

            // Ratings
            var ratingAgg = response.aggregations().get("by_rating");
            if (ratingAgg != null) {
                List<BookFacetsDTO.FacetBucket> ratings = new ArrayList<>();
                for (LongTermsBucket bucket : ratingAgg.lterms().buckets().array()) {
                    ratings.add(new BookFacetsDTO.FacetBucket(
                            String.valueOf(bucket.key()),
                            bucket.docCount()
                    ));
                }
                facets.setRatings(ratings);
            }

            // Rango de precios
            var priceAgg = response.aggregations().get("price_range");
            if (priceAgg != null) {
                var stats = priceAgg.stats();
                facets.setPriceRange(new BookFacetsDTO.PriceRange(stats.min(), stats.max()));
            }

            // Libros con stock
            var stockAgg = response.aggregations().get("with_stock");
            if (stockAgg != null) {
                facets.setBooksWithStock(stockAgg.filter().docCount());
            }

            return facets;
        } catch (IOException e) {
            throw new RuntimeException("Error al obtener facets de OpenSearch", e);
        }
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────────

    private String buildTextQuery(BookSearchParamsDTO params) {
        List<String> parts = new ArrayList<>();
        if (params.getTitle() != null && !params.getTitle().isBlank()) parts.add(params.getTitle());
        if (params.getAuthor() != null && !params.getAuthor().isBlank()) parts.add(params.getAuthor());
        return String.join(" ", parts);
    }

    /**
     * Calcula facets en memoria sobre una lista de documentos ya filtrados.
     * Así el frontend recibe en una sola respuesta: resultados + opciones de filtro disponibles.
     */
    private BookFacetsDTO buildFacetsFromDocs(List<BookDocument> docs) {
        BookFacetsDTO facets = new BookFacetsDTO();

        // Categorías: agrupar y contar
        Map<String, Long> categoryCount = docs.stream()
                .filter(d -> d.getCategory() != null)
                .collect(Collectors.groupingBy(BookDocument::getCategory, Collectors.counting()));
        facets.setCategories(categoryCount.entrySet().stream()
                .map(e -> new BookFacetsDTO.FacetBucket(e.getKey(), e.getValue()))
                .sorted((a, b) -> Long.compare(b.getCount(), a.getCount()))
                .collect(Collectors.toList()));

        // Ratings: agrupar y contar
        Map<String, Long> ratingCount = docs.stream()
                .filter(d -> d.getRating() != null)
                .collect(Collectors.groupingBy(d -> String.valueOf(d.getRating()), Collectors.counting()));
        facets.setRatings(ratingCount.entrySet().stream()
                .map(e -> new BookFacetsDTO.FacetBucket(e.getKey(), e.getValue()))
                .sorted((a, b) -> b.getKey().compareTo(a.getKey()))
                .collect(Collectors.toList()));

        // Rango de precios: min y max
        docs.stream()
                .filter(d -> d.getPrice() != null)
                .mapToDouble(d -> d.getPrice().doubleValue())
                .summaryStatistics();
        var priceStats = docs.stream()
                .filter(d -> d.getPrice() != null)
                .mapToDouble(d -> d.getPrice().doubleValue())
                .summaryStatistics();
        if (priceStats.getCount() > 0) {
            facets.setPriceRange(new BookFacetsDTO.PriceRange(priceStats.getMin(), priceStats.getMax()));
        }

        // Libros con stock > 0
        long withStock = docs.stream()
                .filter(d -> d.getStock() != null && d.getStock() > 0)
                .count();
        facets.setBooksWithStock(withStock);

        return facets;
    }
}
