package com.mishanin.springdata;

import com.mishanin.springdata.entities.ProductGroup;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.runners.Parameterized.*;

@RunWith(Parameterized.class)
public class TestProductGroup {

    @Parameters
    public static Collection<Object[]> data(){
        return Arrays.asList(new Object[][]{
                {1, "prod1"}, {2, "prod2"}, {3, "prod3"},
        });
    }

    public int id;
    public String title;

    public TestProductGroup(int id, String title){
        this.id = id;
        this.title = title;
    }

    @Test
    public void testEquals(){
        ProductGroup p1 = new ProductGroup();
        p1.setId(Long.valueOf(id));
        p1.setTitle(title);

        ProductGroup p2 = new ProductGroup();
        p2.setId(Long.valueOf(id));
        p2.setTitle(title);

        Assert.assertTrue(p1.equals(p2));
    }
}
