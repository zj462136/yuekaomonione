package com.example.lenovo.yuekaomonione.component;

import com.example.lenovo.yuekaomonione.module.HttpModule;
import com.example.lenovo.yuekaomonione.ui.cart.CartActivity;
import com.example.lenovo.yuekaomonione.ui.search.SearchActivity;
import com.example.lenovo.yuekaomonione.ui.xiangqing.ShowsActivity;

import dagger.Component;

@Component(modules = HttpModule.class)
public interface HttpComponent {
    void inject(SearchActivity searchActivity);

    void inject(ShowsActivity showsActivity);

    void inject(CartActivity cartActivity);
}
