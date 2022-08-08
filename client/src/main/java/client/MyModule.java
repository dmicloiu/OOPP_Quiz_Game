/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client;

import client.scenes.*;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;

public class MyModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(MainCtrl.class).in(Scopes.SINGLETON);
        binder.bind(FinalScreenCtrl.class).in(Scopes.SINGLETON);
        binder.bind(MainMenuCtrl.class).in(Scopes.SINGLETON);
        binder.bind(MCQuestionCtrl.class).in(Scopes.SINGLETON);
        binder.bind(MultiLeaderboardCtrl.class).in(Scopes.SINGLETON);
        binder.bind(OQCtrl.class).in(Scopes.SINGLETON);
        binder.bind(SingleLeaderboardCtrl.class).in(Scopes.SINGLETON);
        binder.bind(SplashScreenCtrl.class).in(Scopes.SINGLETON);
        binder.bind(WaitingRoomCtrl.class).in(Scopes.SINGLETON);
        binder.bind(MultiPlayerCtrl.class).in(Scopes.SINGLETON);
        binder.bind(SinglePlayerCtrl.class).in(Scopes.SINGLETON);
        binder.bind(ActivitiesOverviewCtrl.class).in(Scopes.SINGLETON);
        binder.bind(ActivitiesEditCtrl.class).in(Scopes.SINGLETON);
    }
}