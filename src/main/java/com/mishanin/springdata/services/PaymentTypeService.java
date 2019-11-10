package com.mishanin.springdata.services;

import com.mishanin.springdata.entities.PaymentType;
import com.mishanin.springdata.repositories.PaymentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PaymentTypeService {

    private PaymentTypeRepository paymentTypeRepository;

    @Autowired
    public void setPaymentTypeRepository(PaymentTypeRepository paymentTypeRepository) {
        this.paymentTypeRepository = paymentTypeRepository;
    }

    @Transactional(readOnly = true)
    public List<PaymentType> findAll() {return paymentTypeRepository.findAll();}

    @Transactional(readOnly = true)
    public PaymentType findById(Long id) {return paymentTypeRepository.findById(id).get();}
}
