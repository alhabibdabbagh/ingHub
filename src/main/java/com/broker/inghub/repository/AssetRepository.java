package com.broker.inghub.repository;

import com.broker.inghub.model.Asset;
import com.broker.inghub.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    List<Asset> findByCustomer(Customer customer);
    Optional<Asset> findByCustomerAndAssetName(Customer customer, String assetName);
}

