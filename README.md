# MultiStateView
Android View that displays different content based on its state

The four different states the view can be in are:
- Content
- Empty
- Error
- Loading

# Using MultiStateView
MultiStateView can be used the same as any other view by adding it a layout file via XML
```xml
<com.kennyc.view.MultiStateView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/multiStateView"
    android:layout_width="match_parent"
    app:msv_errorView="@layout/error_view"
    app:msv_emptyView="@layout/empty_view"
    app:msv_loadingView="@layout/loading_view"
    app:msv_viewState="loading"
    android:layout_height="match_parent">
    
      <ListView
        android:id="@+id/list"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:listitem="@android:layout/simple_list_item_1" />

</com.kennyc.view.MultiStateView>
```
The attributes to set for MultiStateView are
```xml
<attr name="msv_loadingView" format="reference" />
<attr name="msv_emptyView" format="reference" />
<attr name="msv_errorView" format="reference" />
<attr name="msv_viewState" format="enum">
```

msv_loadingView is the view to be used for the [LOADING ViewState](https://github.com/Kennyc1012/MultiStateView/blob/master/library/src/main/java/com/kennyc/view/MultiStateView.java#L34)<br>
msv_emptyView is the view to be used for the [EMPTY ViewSate](https://github.com/Kennyc1012/MultiStateView/blob/master/library/src/main/java/com/kennyc/view/MultiStateView.java#L34)<br>
msv_errorView is the view to be used for the [ERROR ViewState](https://github.com/Kennyc1012/MultiStateView/blob/master/library/src/main/java/com/kennyc/view/MultiStateView.java#L34)<br>
msv_viewState is the [ViewState](https://github.com/Kennyc1012/MultiStateView/blob/master/library/src/main/java/com/kennyc/view/MultiStateView.java#L34) for the MultiStateView<br>
The [CONTENT ViewState](https://github.com/Kennyc1012/MultiStateView/blob/master/library/src/main/java/com/kennyc/view/MultiStateView.java#L34) is determined by whatever is inside of the tags via XML. <b>NOTE**</b> a Content view must be set for the view to function, this is by design. 

License
=======

    Copyright 2015 Kenny Campagna

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
