package com.enviro.assessment.junior.bheki.service;

import com.enviro.assessment.junior.bheki.dto.WithdrawalNoticeRequest;
import com.enviro.assessment.junior.bheki.dto.WithdrawalNoticeResponse;
import com.enviro.assessment.junior.bheki.entity.Product;
import com.enviro.assessment.junior.bheki.entity.WithdrawalNotice;
import com.enviro.assessment.junior.bheki.enumerate.WithdrawalStatus;
import com.enviro.assessment.junior.bheki.exception.ApiException;
import com.enviro.assessment.junior.bheki.mapper.WithdrawalNoticeMapper;
import com.enviro.assessment.junior.bheki.repository.ProductRepository;
import com.enviro.assessment.junior.bheki.repository.WithdrawalNoticeRepository;
import com.enviro.assessment.junior.bheki.service.impl.WithdrawalNoticeCreatorImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WithdrawalNoticeCreatorTests {
    @Mock
    private WithdrawalEligibility withdrawalEligibility;

    @Mock
    private WithdrawalNoticeRepository withdrawalNoticeRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private WithdrawalNoticeMapper withdrawalNoticeMapper;

    @InjectMocks
    private WithdrawalNoticeCreatorImpl withdrawalNoticeCreator;

    @Test
    void createWithdrawalNotice_ShouldCreateNotice_WhenRequestIsValid() {

        // Given
        WithdrawalNoticeRequest request = new WithdrawalNoticeRequest();
        request.setProductId("product-1");
        request.setAmount(5000);

        Product product = new Product();

        WithdrawalNoticeResponse expectedResponse = new WithdrawalNoticeResponse();

        when(productRepository.findById("product-1"))
                .thenReturn(Optional.of(product));

        when(withdrawalEligibility.isEligible(product, 5000))
                .thenReturn(true);

        when(withdrawalNoticeMapper.toResponse(any(WithdrawalNotice.class)))
                .thenReturn(expectedResponse);

        // When
        WithdrawalNoticeResponse response =
                withdrawalNoticeCreator.createWithdrawalNotice(request);

        // Then
        assertSame(expectedResponse, response);

        verify(productRepository).findById("product-1");
        verify(withdrawalEligibility).isEligible(product, 5000);
        verify(withdrawalNoticeRepository).save(any(WithdrawalNotice.class));
        verify(withdrawalNoticeMapper).toResponse(any(WithdrawalNotice.class));
    }

    @Test
    void createWithdrawalNotice_ShouldThrowException_WhenProductDoesNotExist() {

        WithdrawalNoticeRequest request = new WithdrawalNoticeRequest();
        request.setProductId("product-1");

        when(productRepository.findById("product-1"))
                .thenReturn(Optional.empty());

        ApiException exception = assertThrows(
                ApiException.class,
                () -> withdrawalNoticeCreator.createWithdrawalNotice(request)
        );

        assertEquals("Invalid request", exception.getMessage());

        verify(productRepository).findById("product-1");
        verifyNoInteractions(withdrawalEligibility);
        verifyNoInteractions(withdrawalNoticeRepository);
        verifyNoInteractions(withdrawalNoticeMapper);
    }

    @Test
    void createWithdrawalNotice_ShouldThrowException_WhenWithdrawalIsNotEligible() {

        WithdrawalNoticeRequest request = new WithdrawalNoticeRequest();
        request.setProductId("product-1");
        request.setAmount(5000);

        Product product = new Product();

        when(productRepository.findById("product-1"))
                .thenReturn(Optional.of(product));

        when(withdrawalEligibility.isEligible(product, 5000))
                .thenReturn(false);

        ApiException exception = assertThrows(
                ApiException.class,
                () -> withdrawalNoticeCreator.createWithdrawalNotice(request)
        );

        assertEquals("Withdrawal Notice not eligible", exception.getMessage());

        verify(productRepository).findById("product-1");
        verify(withdrawalEligibility).isEligible(product, 5000);

        verifyNoInteractions(withdrawalNoticeMapper);
        verify(withdrawalNoticeRepository, never()).save(any());
    }

    @Test
    void createWithdrawalNotice_ShouldPopulateWithdrawalNoticeCorrectly() {

        WithdrawalNoticeRequest request = new WithdrawalNoticeRequest();
        request.setProductId("product-1");
        request.setAmount(3500);

        Product product = new Product();

        when(productRepository.findById("product-1"))
                .thenReturn(Optional.of(product));

        when(withdrawalEligibility.isEligible(product, 3500))
                .thenReturn(true);

        when(withdrawalNoticeMapper.toResponse(any()))
                .thenReturn(new WithdrawalNoticeResponse());

        ArgumentCaptor<WithdrawalNotice> captor =
                ArgumentCaptor.forClass(WithdrawalNotice.class);

        withdrawalNoticeCreator.createWithdrawalNotice(request);

        verify(withdrawalNoticeRepository).save(captor.capture());

        WithdrawalNotice saved = captor.getValue();

        assertEquals(3500, saved.getAmount());
        assertEquals(product, saved.getProduct());
        assertEquals(WithdrawalStatus.PENDING, saved.getStatus());
        assertNotNull(saved.getCreatedDate());
    }
}
