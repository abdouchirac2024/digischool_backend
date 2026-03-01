package com.digiSchool.digiSchool.Exceptionconfig.model;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
@FilterDef(name = "tenantFilter", parameters = @ParamDef(name = "tenant", type = String.class))
@Filter(name = "tenantFilter", condition = "tenant = :tenant")
public abstract class TenantEntity {

   @Column(name = "tenant", nullable = false)
   protected String tenant;

   public String getTenant() {
      return tenant;
   }

   public void setTenant(String tenant) {
      this.tenant = tenant;
   }
}
