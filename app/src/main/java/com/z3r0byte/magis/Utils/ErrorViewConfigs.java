/*
 * Copyright 2016 Bas van den Boom 'Z3r0byte'
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.z3r0byte.magis.Utils;

import tr.xip.errorview.ErrorView;


public class ErrorViewConfigs {


    public static ErrorView.Config NoLessonConfig = ErrorView.Config.create()
            .title("Geen lessen")
            .subtitle("")
            .retryVisible(false)
            .build();

    public static ErrorView.Config NoCacheConfig = ErrorView.Config.create()
            .title("Geen cache")
            .subtitle("Er is geen cache beschikbaar")
            .retryVisible(false)
            .build();

    public static ErrorView.Config NoHomeworkConfig = ErrorView.Config.create()
            .title("Geen Huiswerk")
            .subtitle("")
            .retryVisible(false)
            .build();

    public static ErrorView.Config NoGradesConfig = ErrorView.Config.create()
            .title("Geen cijfers")
            .subtitle("")
            .retryVisible(false)
            .build();

    public static ErrorView.Config NoNewGradesConfig = ErrorView.Config.create()
            .title("Geen nieuwe cijfers")
            .subtitle("")
            .retryVisible(false)
            .build();

    public static ErrorView.Config NotLoggedInConfig = ErrorView.Config.create()
            .title("Niet ingelogd")
            .subtitle("Ga naar Agenda en swipe naar beneden.")
            .retryVisible(false)
            .build();
}
