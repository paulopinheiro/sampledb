package br.com.paulopinheiro.sampledb.core.service;

import br.com.paulopinheiro.sampledb.core.dto.DiscountCodeInput;
import br.com.paulopinheiro.sampledb.persistence.entity.DiscountCode;
import java.util.List;

public interface DiscountCodeService {
    List<DiscountCode> getAllDiscountCodes();
    
    void saveDiscountCode(DiscountCodeInput input);
    DiscountCode getDiscountCodeByCode(String code);
    void removeDiscountCode(String discountCode);
}
