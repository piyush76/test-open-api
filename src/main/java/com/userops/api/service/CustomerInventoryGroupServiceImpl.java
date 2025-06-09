package com.userops.api.service;

import com.userops.api.model.CustomerInventoryGroup;
import com.userops.api.repository.CustomerInventoryGroupDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomerInventoryGroupServiceImpl implements CustomerInventoryGroupService {

    private final CustomerInventoryGroupDao customerInventoryGroupDao;

    public CustomerInventoryGroupServiceImpl(CustomerInventoryGroupDao customerInventoryGroupDao) {
        this.customerInventoryGroupDao = customerInventoryGroupDao;
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerInventoryGroup getCustomerInventoryGroup(String companyId, String inventoryGroup, String stockingMethod) {
        return customerInventoryGroupDao.getCustomerInventoryGroup(companyId, inventoryGroup, stockingMethod);
    }

    @Override
    public CustomerInventoryGroup updateCustomerInventoryGroup(String companyId, String inventoryGroup, String stockingMethod, CustomerInventoryGroup entity) {
        return customerInventoryGroupDao.updateCustomerInventoryGroup(companyId, inventoryGroup, stockingMethod, entity);
    }
}
