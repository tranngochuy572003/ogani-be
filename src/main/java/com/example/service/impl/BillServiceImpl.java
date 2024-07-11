package com.example.service.impl;

import com.example.dto.*;
import com.example.entity.Bill;
import com.example.entity.BillDetail;
import com.example.entity.User;
import com.example.exception.BadRequestException;
import com.example.mapper.BillDetailMapper;
import com.example.repository.BillRepository;
import com.example.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.common.MessageConstant.VALUE_NO_EXIST;

@Service
public class BillServiceImpl implements BillService {
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private CartService cartService;
    @Autowired
    private ProductService productService;
    @Autowired
    private BillDetailService billDetailService;
    @Autowired
    private UserService userService;


    @Override
    public BillDto getBillById(String billId) {
        Optional<Bill> bill = billRepository.findById(billId);
        List<BillDetailDto> billDetailDtoList = new ArrayList<>();
        if(bill.isPresent()){
            CartDto cartDto = cartService.getByUserId(bill.get().getUsers().getId());
            List<CartDetailInfoDto> cartDetailInfoDtoList = cartDto.getCartDetail();

            for(CartDetailInfoDto cartDetailInfoDto : cartDetailInfoDtoList){
                String imageUrl = productService.getProductById(cartDetailInfoDto.getProductId()).getImageList().get(0);
                BillDetailDto billDetailDto = new BillDetailDto(cartDetailInfoDto.getQuantity(),cartDetailInfoDto.getPrice(),cartDetailInfoDto.getName(),imageUrl);
                billDetailDtoList.add(billDetailDto);
                BillDetail billDetail = BillDetailMapper.toEntity(billDetailDto);
                billDetail.setBills(bill.get());
                billDetailService.save(billDetail);
            }
            List<BillDetail> billDetails = billDetailService.findByBillsId(billId);
            Long total = 0L;

            for(BillDetail billDetail :billDetails){
                total+=billDetail.getPrice();
            }
            double totalPrice = total + bill.get().getTax()*total;
            bill.get().setTotalPrice((long) totalPrice);
            billRepository.save(bill.get());

            LocalDateTime currentDate = LocalDateTime.now();
            return new BillDto(bill.get().getId(),bill.get().getUsers().getId(),currentDate,cartDto.getTotalPrice(),billDetailDtoList);
        }
        else {
            throw new BadRequestException(VALUE_NO_EXIST);
        }
    }
    @Override
    public Bill confirmOrder(OrderDto orderDto , String userId){
        Bill bill = new Bill();
        User user =  userService.findUserById(userId);
        bill.setTax(orderDto.getTax());
        bill.setConfirm(orderDto.isConfirm());
        bill.setUsers(user);
        billRepository.save(bill);
        return bill;
    }
}