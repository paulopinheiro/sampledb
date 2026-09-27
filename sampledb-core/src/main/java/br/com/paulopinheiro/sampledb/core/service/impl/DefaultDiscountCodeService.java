package br.com.paulopinheiro.sampledb.core.service.impl;

import br.com.paulopinheiro.sampledb.core.dto.DiscountCodeInput;
import br.com.paulopinheiro.sampledb.core.service.DiscountCodeService;
import br.com.paulopinheiro.sampledb.persistence.dao.impl.DiscountCodeDao;
import br.com.paulopinheiro.sampledb.persistence.entity.DiscountCode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

/**
 * Default implementation of the DiscountCode business service using pure Jakarta CDI.
 */
@ApplicationScoped 
public class DefaultDiscountCodeService implements DiscountCodeService {
    @Inject private DiscountCodeDao dao;

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<DiscountCode> getAllDiscountCodes() {
        return dao.findAll();
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public DiscountCode getDiscountCodeByCode(String code) {
        return dao.findDiscountCodeByCode(code);
    }

    @Override
    @Transactional
    public void saveDiscountCode(DiscountCodeInput input) { // Safe, immutable contract
        if (input == null) throw new IllegalArgumentException("Discount Code input cannot be null");
        /* Primary key is not automatic */
        if (input.discountCode()== null || input.discountCode().isEmpty()) throw new IllegalArgumentException("Code cannot be null");
        
        DiscountCode existing = this.getDiscountCodeByCode(input.discountCode());
        
        if (existing == null) { // It's a new discount code
            dao.create(new DiscountCode(input.discountCode(),input.rate()));
        } else { //It's an existing discount code
            existing.setRate(input.rate());
            dao.edit(existing);
        }
    }

    @Override
    @Transactional
    public void removeDiscountCode(String discountCodeCode) {
        if (discountCodeCode==null || discountCodeCode.isEmpty()) throw new IllegalArgumentException("The code must be informed");

        DiscountCode discountCode = dao.find(discountCodeCode);

        if (discountCode!=null) dao.remove(discountCode);
    }
}
