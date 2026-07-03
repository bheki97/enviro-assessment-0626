package com.enviro.assessment.junior.bheki.config;

import com.enviro.assessment.junior.bheki.entity.InvestmentPortfolio;
import com.enviro.assessment.junior.bheki.entity.Product;
import com.enviro.assessment.junior.bheki.entity.WithdrawalNotice;
import com.enviro.assessment.junior.bheki.repository.InvestmentPortfolioRepository;
import com.enviro.assessment.junior.bheki.repository.ProductRepository;
import com.enviro.assessment.junior.bheki.repository.WithdrawalNoticeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.codec.json.GsonDecoder;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.InputStream;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class InvestmentPortfolioDatabaseLoader implements CommandLineRunner{
//

    private final ObjectMapper objectMapper;
    private final InvestmentPortfolioRepository investmentPortfolioRepository;
    private final ProductRepository productRepository;
    private final WithdrawalNoticeRepository withdrawalNoticeRepository;
//    private final Gson gson;
    @Value("${portfolio.count}")
    private int portfolioCount;

    //@Override
    public void run(String... args) throws Exception {
        Resource resource;
        for(int i=1;i<=portfolioCount;i++){

            InvestmentPortfolio investmentPortfolio;
            resource = new ClassPathResource("portfolio-"+i+".json");
            try (InputStream input = resource.getInputStream()){
                investmentPortfolio = objectMapper.readValue(input, InvestmentPortfolio.class);
                investmentPortfolio = investmentPortfolioRepository.save(investmentPortfolio);
            }
            resource = new ClassPathResource("products-"+i+".json");
            List<Product> products;

            try (InputStream input = resource.getInputStream()){
                products = objectMapper.readValue(input, new TypeReference<>() {});
                InvestmentPortfolio finalInvestmentPortfolio = investmentPortfolio;

                products = products.stream().peek(prod->
                    prod.setInvestmentPortfolio(finalInvestmentPortfolio)
                ).toList();
                productRepository.saveAll(products);
            }

        }


    }


}
