/*
 * Copyright 2015 Igor Morais
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.epitrack.guardioes.view.base;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author Igor Morais
 */
public interface INavigate {

    void navigateTo(Class<? extends Activity> activityClass);

    void navigateTo(Class<? extends Activity> activityClass, int flags);

    void navigateTo(Class<? extends Activity> activityClass, Bundle bundle);

    void navigateTo(Class<? extends Activity> activityClass, int flags, Bundle bundle);

    void navigateForResult(Class<? extends Activity> activityClass, int requestCode);

    void navigateForResult(Class<? extends Activity> activityClass, int requestCode, int flags);

    void navigateForResult(Class<? extends Activity> activityClass, int requestCode, Bundle bundle);

    void navigateForResult(Class<? extends Activity> activityClass, int requestCode, int flags, Bundle bundle);
}
