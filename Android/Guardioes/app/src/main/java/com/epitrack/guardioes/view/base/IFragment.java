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
 
import android.app.Fragment;
import android.os.Bundle;

/**
 * @author Igor Morais
 */
public interface IFragment {

    int getLayout();

    Fragment getCurrentFragment();

    int add(Class<? extends Fragment> fragmentClass);

    int add(Class<? extends Fragment> fragmentClass, String tag);

    int add(Class<? extends Fragment> fragmentClass, String tag, Bundle bundle);

    int add(Class<? extends Fragment> fragmentClass, String tag, boolean addToBackStack);

    int add(Class<? extends Fragment> fragmentClass, Bundle bundle, boolean addToBackStack);

    int add(Class<? extends Fragment> fragmentClass, String tag, Bundle bundle, boolean addToBackStack);

    int replace(Class<? extends Fragment> fragmentClass);

    int replace(Class<? extends Fragment> fragmentClass, String tag);

    int replace(Class<? extends Fragment> fragmentClass, String tag, Bundle bundle);

    int replace(Class<? extends Fragment> fragmentClass, String tag, boolean addToBackStack);

    int replace(Class<? extends Fragment> fragmentClass, Bundle bundle, boolean addToBackStack);

    int replace(Class<? extends Fragment> fragmentClass, String tag, Bundle bundle, boolean addToBackStack);

    int remove(Fragment fragment);
}
