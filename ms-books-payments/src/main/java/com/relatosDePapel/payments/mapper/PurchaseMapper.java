package com.relatosDePapel.payments.mapper;

import com.relatosDePapel.payments.dto.CustomerDTO;
import com.relatosDePapel.payments.dto.PurchaseItemDTO;
import com.relatosDePapel.payments.dto.PurchaseResponseDTO;
import com.relatosDePapel.payments.entity.Customer;
import com.relatosDePapel.payments.entity.Purchase;
import com.relatosDePapel.payments.entity.PurchaseItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PurchaseMapper {

    public CustomerDTO toCustomerDTO(Customer customer) {
        if (customer == null) {
            return null;
        }
        return new CustomerDTO(
                customer.getName(),
                customer.getEmail(),
                customer.getAddress()
        );
    }

    public Customer toCustomer(CustomerDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Customer(
                dto.getName(),
                dto.getEmail(),
                dto.getAddress()
        );
    }

    public PurchaseItemDTO toItemDTO(PurchaseItem item) {
        if (item == null) {
            return null;
        }
        return new PurchaseItemDTO(
                item.getBookId(),
                item.getQuantity()
        );
    }

    public PurchaseResponseDTO toResponseDTO(Purchase purchase) {
        if (purchase == null) {
            return null;
        }

        CustomerDTO customerDTO = toCustomerDTO(purchase.getCustomer());

        List<PurchaseItemDTO> itemsDTO = purchase.getItems().stream()
                .map(this::toItemDTO)
                .collect(Collectors.toList());

        return new PurchaseResponseDTO(
                purchase.getId(),
                customerDTO,
                itemsDTO,
                purchase.getStatus(),
                purchase.getCreatedAt()
        );
    }
}
