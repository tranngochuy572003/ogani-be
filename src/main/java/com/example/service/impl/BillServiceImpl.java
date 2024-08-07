package com.example.service.impl;

import com.example.dto.BillDetailDto;
import com.example.dto.BillDto;
import com.example.dto.CartDetailInfoDto;
import com.example.dto.CartDto;
import com.example.entity.*;
import com.example.exception.BadRequestException;
import com.example.mapper.BillDetailMapper;
import com.example.repository.BillRepository;
import com.example.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.common.MessageConstant.ORDER_CONFIRMED;
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
        if (bill.isPresent()) {
            long totalAmount = 0L;
            List<BillDetail> billDetails = bill.get().getBillDetailList();
            for(BillDetail billDetail:billDetails){
                totalAmount+=billDetail.getPrice()*billDetail.getQuantity();
            }
            return new BillDto(billId,bill.get().getUsers().getId(),bill.get().getCreatedDate(),totalAmount,bill.get().isConfirm(),BillDetailMapper.toListDto(billDetails));

        } else {
            throw new BadRequestException(VALUE_NO_EXIST);
        }
    }
    @Override
    public void order(String userId) {
        List<BillDetailDto> billDetailDtoList = new ArrayList<>();
        User user = userService.findUserById(userId);

        Bill bill = new Bill();
        bill.setUsers(user);
        billRepository.save(bill);

        List<CartDetailInfoDto> cartDetailAddBill = new ArrayList<>();
        CartDto cartDto = cartService.getByUserId(userId);
        List<CartDetailInfoDto> cartDetailInfoDtoList = cartDto.getCartDetail();

        for (CartDetailInfoDto cartDetailInfoDto : cartDetailInfoDtoList) {
            if(cartDetailInfoDto.isChosen()){
                cartDetailAddBill.add(cartDetailInfoDto);
            }
        }

        for (CartDetailInfoDto cartDetail : cartDetailAddBill) {
            String imageUrl = productService.getProductById(cartDetail.getProductId()).getImageList().get(0);
            BillDetailDto billDetailDto = new BillDetailDto(cartDetail.getQuantity(), cartDetail.getPrice(), cartDetail.getName(), imageUrl);
            billDetailDtoList.add(billDetailDto);
            BillDetail billDetail = BillDetailMapper.toEntity(billDetailDto);
            billDetail.setBills(bill);
            billDetailService.save(billDetail);
        }

        Long total = 0L;
        for (BillDetail billDetail : BillDetailMapper.toListEntity(billDetailDtoList)) {
            total += billDetail.getPrice();
        }
        bill.setTotalPrice(total);
        billRepository.save(bill);

        Cart cart = cartService.getCartByUserId(bill.getUsers().getId());
        List<CartDetail> cartDetailList =cart.getCartDetails();
        cartDetailList.removeIf(CartDetail::isChosen);
        cartService.save(cart);
        if(cartDetailList.isEmpty()){
            cartService.deleteCartById(cart.getId());
        }
        cartService.save(cart);
    }

    @Override
    public void confirmOrder(String billId) {
       Optional<Bill> bill = billRepository.findById(billId);
       if(bill.isPresent()){
           if(bill.get().isConfirm()){
               throw new BadRequestException(ORDER_CONFIRMED);
           }
           bill.get().setConfirm(true);
           List<BillDetail> billDetailList =bill.get().getBillDetailList();
           for(BillDetail billDetail : billDetailList){
               String nameProduct = billDetail.getNameProduct();
               Product product = productService.findProductByName(nameProduct);
               product.setInventory(product.getInventory()-billDetail.getQuantity());
               productService.save(product);
           }
           billRepository.save(bill.get());

       }else {
           throw new BadRequestException(VALUE_NO_EXIST);
       }
    }
    @Override
    public List<BillDto> getBillByUserId(String userId) {
        User user= userService.findUserById(userId);
        List<Bill> billList = billRepository.findByUsers(user);
        List<BillDto> billDtoList = new ArrayList<>();
        for (Bill bill : billList) {
            BillDto billDto = getBillById(bill.getId());
            billDtoList.add(billDto);
        }
        return billDtoList;
    }

    @Override
    public boolean isAuthorizedToGetBill(String billId,String userId) {
        Optional<Bill> bill = billRepository.findById(billId);
        if(bill.isPresent()){
            return bill.get().getUsers().getId().equals(userId);
        }
        else {
            throw new BadRequestException(VALUE_NO_EXIST);
        }
    }
}