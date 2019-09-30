package com.mishanin.springdata.utils;

import com.mishanin.springdata.entities.OrderDetails;
import com.mishanin.springdata.entities.Product;

import java.math.BigDecimal;
import java.util.*;

public class GroupOrderDetails {

    private TreeMap<Long, OrderDetails> products;

    public GroupOrderDetails() {
        this.products = new TreeMap<>();
    }

    public void addProduct(Product product){
        //если продукт присутствует в корзине, то увеличиваем кол-во штук на 1
        if(products.computeIfPresent(product.getId(),(key, oldValue)->{
            oldValue.setCount(oldValue.getCount()+1);
            return oldValue;}) == null) {
            //иначе добавляем в корзину новую пару ключ-значение: "продукт - 1шт"
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setCount(1);
            orderDetails.setProductCost(product.getPrice());
            orderDetails.setProduct(product);
            products.put(product.getId(), orderDetails);
        }
    }

    public void removeProduct(Product product){
        //уменьшаем количество продуктов в группе на 1 шт
        products.computeIfPresent(product.getId(),(key, oldValue)-> {
            oldValue.setCount(oldValue.getCount()-1);
            return oldValue;});
        //если в группе продуктов осталось 0 штук, то удаляем группу из мапы
        if (products.get(product.getId()).getCount()<=0)
            products.remove(product.getId());
    }

    public Set<Map.Entry<Long, OrderDetails>> getOrderDetails() {

        //products.entrySet().stream().collect()
        return products.entrySet();
    }

    public List<OrderDetails> getListOrderDetails() {

        return products.entrySet().stream()
                .map(o->o.getValue())
                .collect(ArrayList::new,
                        ArrayList::add,
                        ArrayList::addAll);
    }

    public double getTotalCost(){
        return products.entrySet().stream().filter(a->a!=null)
                //получаем произведение цены товара на кол-во штук
                .map((a) -> a.getValue().getProductCost().multiply(BigDecimal.valueOf(a.getValue().getCount())))
                //складываем суммарную стоимость каждой группы товара
                .reduce((a,b)->a.add(b))
                .orElse(BigDecimal.ZERO).doubleValue();
    }

    public void clear(){
        this.products = new TreeMap<>();
    }

//    //сортировка по наименованию продукта (А-Я)
//    class ProductComparator implements Comparator<OrderDetails> {
//        @Override
//        public int compare(OrderDetails o1, OrderDetails o2) {
//            return o1.getProduct().getTitle().compareTo(o2.getProduct().getTitle());
//        }
//    }
}
