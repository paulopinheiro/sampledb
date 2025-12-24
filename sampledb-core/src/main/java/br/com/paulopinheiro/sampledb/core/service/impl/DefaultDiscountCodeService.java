package br.com.paulopinheiro.sampledb.core.service.impl;

import br.com.paulopinheiro.sampledb.core.service.DiscountCodeService;
import br.com.paulopinheiro.sampledb.persistence.dao.impl.DiscountCodeDao;
import br.com.paulopinheiro.sampledb.persistence.entity.DiscountCode;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.util.List;
import java.util.Optional;

@Stateless
public class DefaultDiscountCodeService implements DiscountCodeService {
    @EJB private DiscountCodeDao dao;

    @Override
    public List<DiscountCode> getAllDiscountCodes() {
        return dao.findAll();
    }

    @Override
    public void saveDiscountCode(DiscountCode discountCode) {
        if (Optional.ofNullable(discountCode).isEmpty()) throw new IllegalArgumentException("Discount code can't be null");

        if (Optional.ofNullable(discountCode).isEmpty()) dao.create(discountCode);
        else dao.edit(discountCode);
    }

    @Override
    public DiscountCode getDiscountCodeByCode(String code) {
        return dao.findDiscountCodeByCode(code);
    }

    @Override
    public void removeDiscountCode(DiscountCode discountCode) {
        if (Optional.ofNullable(discountCode).isEmpty()) throw new IllegalArgumentException("Discount code can't be null");

        dao.remove(discountCode);
    }
}
