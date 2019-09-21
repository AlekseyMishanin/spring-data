package com.mishanin.springdata.utils;

import com.mishanin.springdata.entities.Product;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class GroupProduct {

    private TreeMap<Product, Integer> products;

    public GroupProduct() {
        this.products = new TreeMap<>(new ProductComparator());
    }

    public void addProduct(Product product){
        //если продукт присутствует в корзине, то увеличиваем кол-во штук на 1
        if(products.computeIfPresent(product,(key, oldValue)->++oldValue) == null) {
            //иначе добавляем в корзину новую пару ключ-значение: "продукт - 1шт"
            products.put(product, 1);
        }
    }

    public void removeProduct(Product product){
        //уменьшаем количество продуктов в группе на 1 шт
        products.computeIfPresent(product,(key, oldValue)-> --oldValue);
        //если в группе продуктов осталось 0 штук, то удаляем группу из мапы
        if (products.get(product).intValue()<=0)
            products.remove(product);
    }

    public Set<Map.Entry<Product, Integer>> getProducts() {
        return products.entrySet();
    }

    public int getTotalCost(){
        return products.entrySet().stream()
                //получаем произведение цены товара на кол-во штук
                .map((a) -> a.getValue()*a.getKey().getPrice())
                //складываем суммарную стоимость каждой группы товара
                .reduce(0,(a,b)->a+b);
    }

    public void clear(){
        this.products = new TreeMap<>(new ProductComparator());
    }

    //сортировка по наименованию продукта (А-Я)
    class ProductComparator implements Comparator<Product> {
        @Override
        public int compare(Product o1, Product o2) {
            return o1.getTitle().compareTo(o2.getTitle());
        }
    }
}
