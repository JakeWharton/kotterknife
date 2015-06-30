package butterknife

import android.app.Activity
import android.app.Dialog
import android.app.Fragment
import android.support.v4.app.Fragment as SupportFragment
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.View
import kotlin.properties.ReadOnlyProperty

public fun <V : View> View.bindView(id: Int)
    : ReadOnlyProperty<View, V> = required(id, ::findViewById)
public fun <V : View> Activity.bindView(id: Int)
    : ReadOnlyProperty<Activity, V> = required(id, ::findViewById)
public fun <V : View> Dialog.bindView(id: Int)
    : ReadOnlyProperty<Dialog, V> = required(id, ::findViewById)
public fun <V : View> Fragment.bindView(id: Int)
    : ReadOnlyProperty<Fragment, V> = required(id, ::findViewById)
public fun <V : View> SupportFragment.bindView(id: Int)
    : ReadOnlyProperty<SupportFragment, V> = required(id, ::findViewById)
public fun <V : View> ViewHolder.bindView(id: Int)
    : ReadOnlyProperty<ViewHolder, V> = required(id, ::findViewById)

public fun <V : View> View.bindOptionalView(id: Int)
    : ReadOnlyProperty<View, V?> = optional(id, ::findViewById)
public fun <V : View> Activity.bindOptionalView(id: Int)
    : ReadOnlyProperty<Activity, V?> = optional(id, ::findViewById)
public fun <V : View> Dialog.bindOptionalView(id: Int)
    : ReadOnlyProperty<Dialog, V?> = optional(id, ::findViewById)
public fun <V : View> Fragment.bindOptionalView(id: Int)
    : ReadOnlyProperty<Fragment, V?> = optional(id, ::findViewById)
public fun <V : View> SupportFragment.bindOptionalView(id: Int)
    : ReadOnlyProperty<SupportFragment, V?> = optional(id, ::findViewById)
public fun <V : View> ViewHolder.bindOptionalView(id: Int)
    : ReadOnlyProperty<ViewHolder, V?> = optional(id, ::findViewById)

public fun <V : View> View.bindViews(vararg ids: Int)
    : ReadOnlyProperty<View, List<V>> = required(ids, ::findViewById)
public fun <V : View> Activity.bindViews(vararg ids: Int)
    : ReadOnlyProperty<Activity, List<V>> = required(ids, ::findViewById)
public fun <V : View> Dialog.bindViews(vararg ids: Int)
    : ReadOnlyProperty<Dialog, List<V>> = required(ids, ::findViewById)
public fun <V : View> Fragment.bindViews(vararg ids: Int)
    : ReadOnlyProperty<Fragment, List<V>> = required(ids, ::findViewById)
public fun <V : View> SupportFragment.bindViews(vararg ids: Int)
    : ReadOnlyProperty<SupportFragment, List<V>> = required(ids, ::findViewById)
public fun <V : View> ViewHolder.bindViews(vararg ids: Int)
    : ReadOnlyProperty<ViewHolder, List<V>> = required(ids, ::findViewById)

public fun <V : View> View.bindOptionalViews(vararg ids: Int)
    : ReadOnlyProperty<View, List<V>> = optional(ids, ::findViewById)
public fun <V : View> Activity.bindOptionalViews(vararg ids: Int)
    : ReadOnlyProperty<Activity, List<V>> = optional(ids, ::findViewById)
public fun <V : View> Dialog.bindOptionalViews(vararg ids: Int)
    : ReadOnlyProperty<Dialog, List<V>> = optional(ids, ::findViewById)
public fun <V : View> Fragment.bindOptionalViews(vararg ids: Int)
    : ReadOnlyProperty<Fragment, List<V>> = optional(ids, ::findViewById)
public fun <V : View> SupportFragment.bindOptionalViews(vararg ids: Int)
    : ReadOnlyProperty<SupportFragment, List<V>> = optional(ids, ::findViewById)
public fun <V : View> ViewHolder.bindOptionalViews(vararg ids: Int)
    : ReadOnlyProperty<ViewHolder, List<V>> = optional(ids, ::findViewById)

private fun Fragment.findViewById(id: Int): View? = getView().findViewById(id)
private fun SupportFragment.findViewById(id: Int): View? = getView().findViewById(id)
private fun ViewHolder.findViewById(id: Int): View? = itemView.findViewById(id)

private fun viewNotFound(id:Int, desc: PropertyMetadata) =
    throw IllegalStateException("View ID $id for '${desc.name}' not found.")

suppress("UNCHECKED_CAST")
private fun required<T, V : View>(id: Int, finder : T.(Int) -> View?)
    = Lazy { t : T, desc -> t.finder(id) as V? ?: viewNotFound(id, desc) }

suppress("UNCHECKED_CAST")
private fun optional<T, V : View>(id: Int, finder : T.(Int) -> View?)
    = Lazy { t : T, desc ->  t.finder(id) as V? }

suppress("UNCHECKED_CAST")
private fun required<T, V : View>(ids: IntArray, finder : T.(Int) -> View?)
    = Lazy { t : T, desc -> ids.map { t.finder(it) as V? ?: viewNotFound(it, desc) } }

suppress("UNCHECKED_CAST")
private fun optional<T, V : View>(ids: IntArray, finder : T.(Int) -> View?)
    = Lazy { t : T, desc -> ids.map { t.finder(it) as V? }.filterNotNull() }

// Like Kotlin's lazy delegate but the initializer gets the target and metadata passed to it
private class Lazy<T, V>(private val initializer : (T, PropertyMetadata) -> V) : ReadOnlyProperty<T, V> {
  private object EMPTY
  private var value: Any? = EMPTY

  override fun get(thisRef: T, desc: PropertyMetadata): V {
    if (value == EMPTY) {
      value = initializer(thisRef, desc)
    }
    @suppress("UNCHECKED_CAST")
    return value as V
  }
}
