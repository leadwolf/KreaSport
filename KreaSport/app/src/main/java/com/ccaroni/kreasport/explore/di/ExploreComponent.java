package com.ccaroni.kreasport.explore.di;

import com.ccaroni.kreasport.explore.services.impl.RaceService;
import com.ccaroni.kreasport.explore.vm.AbstractExploreVM;
import com.ccaroni.kreasport.explore.vm.impl.ExploreVM;

import dagger.Subcomponent;

/**
 * Created by Master on 24/02/2018.
 */
@ExploreScope
@Subcomponent(modules = {ExploreModule.class})
public interface ExploreComponent {

    void inject(AbstractExploreVM abstractExploreVM);

    void inject(ExploreVM exploreVM);

    void inject(RaceService raceService);

}
