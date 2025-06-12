package com.incora.api.iam.repository;

import com.incora.api.iam.model.AdminRole;
import com.incora.api.iam.model.UserOpsEntity;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JooqUserOpsEntityDao implements UserOpsEntityDao {

    private final DSLContext dslContext;

    @Autowired
    public JooqUserOpsEntityDao(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public List<UserOpsEntity> findAll() {
        return dslContext.select()
                .from("USER_OPS_ENTITY")
                .fetch(record -> {
                    UserOpsEntity entity = new UserOpsEntity();
                    entity.setPersonnelId(record.get("PERSONNEL_ID", Long.class));
                    entity.setCompanyId(record.get("COMPANY_ID", String.class));
                    entity.setOpsEntityId(record.get("OPS_ENTITY_ID", String.class));
                    entity.setOpsCompanyId(record.get("OPS_COMPANY_ID", String.class));
                    String adminRoleStr = record.get("ADMIN_ROLE", String.class);
                    if (adminRoleStr != null) {
                        entity.setAdminRole(AdminRole.valueOf(adminRoleStr));
                    }
                    return entity;
                });
    }

    @Override
    public Optional<UserOpsEntity> findById(Long personnelId, String companyId, String opsEntityId) {
        return dslContext.select()
                .from("USER_OPS_ENTITY")
                .where("PERSONNEL_ID = ? AND COMPANY_ID = ? AND OPS_ENTITY_ID = ?", personnelId, companyId, opsEntityId)
                .fetchOptional(record -> {
                    UserOpsEntity entity = new UserOpsEntity();
                    entity.setPersonnelId(record.get("PERSONNEL_ID", Long.class));
                    entity.setCompanyId(record.get("COMPANY_ID", String.class));
                    entity.setOpsEntityId(record.get("OPS_ENTITY_ID", String.class));
                    entity.setOpsCompanyId(record.get("OPS_COMPANY_ID", String.class));
                    String adminRoleStr = record.get("ADMIN_ROLE", String.class);
                    if (adminRoleStr != null) {
                        entity.setAdminRole(AdminRole.valueOf(adminRoleStr));
                    }
                    return entity;
                });
    }

    @Override
    public UserOpsEntity save(UserOpsEntity userOpsEntity) {
        String adminRoleValue = userOpsEntity.getAdminRole() != null ? 
                userOpsEntity.getAdminRole().name() : null;

        int result = dslContext.insertInto(dslContext.table("USER_OPS_ENTITY"))
                .set(dslContext.field("PERSONNEL_ID"), userOpsEntity.getPersonnelId())
                .set(dslContext.field("COMPANY_ID"), userOpsEntity.getCompanyId())
                .set(dslContext.field("OPS_ENTITY_ID"), userOpsEntity.getOpsEntityId())
                .set(dslContext.field("OPS_COMPANY_ID"), userOpsEntity.getOpsCompanyId())
                .set(dslContext.field("ADMIN_ROLE"), adminRoleValue)
                .onDuplicateKeyUpdate()
                .set(dslContext.field("OPS_COMPANY_ID"), userOpsEntity.getOpsCompanyId())
                .set(dslContext.field("ADMIN_ROLE"), adminRoleValue)
                .execute();

        return userOpsEntity;
    }

    @Override
    public void deleteById(Long personnelId, String companyId, String opsEntityId) {
        dslContext.deleteFrom(dslContext.table("USER_OPS_ENTITY"))
                .where("PERSONNEL_ID = ? AND COMPANY_ID = ? AND OPS_ENTITY_ID = ?", personnelId, companyId, opsEntityId)
                .execute();
    }

    @Override
    public boolean existsById(Long personnelId, String companyId, String opsEntityId) {
        return dslContext.fetchExists(
                dslContext.selectFrom("USER_OPS_ENTITY")
                        .where("PERSONNEL_ID = ? AND COMPANY_ID = ? AND OPS_ENTITY_ID = ?", personnelId, companyId, opsEntityId)
        );
    }
}
