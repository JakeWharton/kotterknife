package butterknife

import android.app.Activity
import android.app.Dialog
import android.app.Fragment
import android.support.v4.app.Fragment as SupportFragment
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.View
import kotlin.properties.ReadOnlyProperty

public fun <V : View> View.bindView(id: Int)
    : ReadOnlyProperty<View, V> = ViewBinding(id, ::findViewById)
public fun <V : View> Activity.bindView(id: Int)
    : ReadOnlyProperty<Activity, V> = ViewBinding(id, ::findViewById)
public fun <V : View> Dialog.bindView(id: Int)
    : ReadOnlyProperty<Dialog, V> = ViewBinding(id, ::findViewById)
public fun <V : View> Fragment.bindView(id: Int)
    : ReadOnlyProperty<Fragment, V> = ViewBinding(id, ::findViewById)
public fun <V : View> SupportFragment.bindView(id: Int)
    : ReadOnlyProperty<SupportFragment, V> = ViewBinding(id, ::findViewById)
public fun <V : View> ViewHolder.bindView(id: Int)
    : ReadOnlyProperty<ViewHolder, V> = ViewBinding(id, ::findViewById)

public fun <V : View> View.bindOptionalView(id: Int)
    : ReadOnlyProperty<View, V?> = OptionalViewBinding(id, ::findViewById)
public fun <V : View> Activity.bindOptionalView(id: Int)
    : ReadOnlyProperty<Activity, V?> = OptionalViewBinding(id, ::findViewById)
public fun <V : View> Dialog.bindOptionalView(id: Int)
    : ReadOnlyProperty<Dialog, V?> = OptionalViewBinding(id, ::findViewById)
public fun <V : View> Fragment.bindOptionalView(id: Int)
    : ReadOnlyProperty<Fragment, V?> = OptionalViewBinding(id, ::findViewById)
public fun <V : View> SupportFragment.bindOptionalView(id: Int)
    : ReadOnlyProperty<SupportFragment, V?> = OptionalViewBinding(id, ::findViewById)
public fun <V : View> ViewHolder.bindOptionalView(id: Int)
    : ReadOnlyProperty<ViewHolder, V?> = OptionalViewBinding(id, ::findViewById)

public fun <V : View> View.bindViews(vararg ids: Int)
    : ReadOnlyProperty<View, List<V>> = ViewListBinding(ids, ::findViewById)
public fun <V : View> Activity.bindViews(vararg ids: Int)
    : ReadOnlyProperty<Activity, List<V>> = ViewListBinding(ids, ::findViewById)
public fun <V : View> Dialog.bindViews(vararg ids: Int)
    : ReadOnlyProperty<Dialog, List<V>> = ViewListBinding(ids, ::findViewById)
public fun <V : View> Fragment.bindViews(vararg ids: Int)
    : ReadOnlyProperty<Fragment, List<V>> = ViewListBinding(ids, ::findViewById)
public fun <V : View> SupportFragment.bindViews(vararg ids: Int)
    : ReadOnlyProperty<SupportFragment, List<V>> = ViewListBinding(ids, ::findViewById)
public fun <V : View> ViewHolder.bindViews(vararg ids: Int)
    : ReadOnlyProperty<ViewHolder, List<V>> = ViewListBinding(ids, ::findViewById)

public fun <V : View> View.bindOptionalViews(vararg ids: Int)
    : ReadOnlyProperty<View, List<V>> = OptionalViewListBinding(ids, ::findViewById)
public fun <V : View> Activity.bindOptionalViews(vararg ids: Int)
    : ReadOnlyProperty<Activity, List<V>> = OptionalViewListBinding(ids, ::findViewById)
public fun <V : View> Dialog.bindOptionalViews(vararg ids: Int)
    : ReadOnlyProperty<Dialog, List<V>> = OptionalViewListBinding(ids, ::findViewById)
public fun <V : View> Fragment.bindOptionalViews(vararg ids: Int)
    : ReadOnlyProperty<Fragment, List<V>> = OptionalViewListBinding(ids, ::findViewById)
public fun <V : View> SupportFragment.bindOptionalViews(vararg ids: Int)
    : ReadOnlyProperty<SupportFragment, List<V>> = OptionalViewListBinding(ids, ::findViewById)
public fun <V : View> ViewHolder.bindOptionalViews(vararg ids: Int)
    : ReadOnlyProperty<ViewHolder, List<V>> = OptionalViewListBinding(ids, ::findViewById)

private fun Fragment.findViewById(id: Int): View? = getView().findViewById(id)
private fun SupportFragment.findViewById(id: Int): View? = getView().findViewById(id)
private fun ViewHolder.findViewById(id: Int): View? = itemView.findViewById(id)

private fun viewNotFound(id:Int, desc: PropertyMetadata) =
    throw IllegalStateException("View ID $id for '${desc.name}' not found.")

private class ViewBinding<T, V : View>(val id: Int, val findView: T.(Int) -> View?)
    : ReadOnlyProperty<T, V> {
  private val lazy = Lazy<V>()

  suppress("UNCHECKED_CAST")
  override fun get(thisRef: T, desc: PropertyMetadata): V
      = lazy.get { thisRef.findView(id) as V? ?: viewNotFound(id, desc) }
}

private class OptionalViewBinding<T, V : View>(val id: Int, val findView: T.(Int) -> View?)
    : ReadOnlyProperty<T, V?> {
  private val lazy = Lazy<V?>()

  suppress("UNCHECKED_CAST")
  override fun get(thisRef: T, desc: PropertyMetadata): V?
      = lazy.get { thisRef.findView(id) as V? }
}

private class ViewListBinding<T, V : View>(val ids: IntArray, val findView: T.(Int) -> View?)
    : ReadOnlyProperty<T, List<V>> {
  private var lazy = Lazy<List<V>>()

  suppress("UNCHECKED_CAST")
  override fun get(thisRef: T, desc: PropertyMetadata): List<V>
      = lazy.get { ids.map { id -> thisRef.findView(id) as V? ?: viewNotFound(id, desc) } }
}

private class OptionalViewListBinding<T, V : View>(val ids: IntArray, val findView: T.(Int) -> View?)
    : ReadOnlyProperty<T, List<V>> {
  private var lazy = Lazy<List<V>>()

  suppress("UNCHECKED_CAST")
  override fun get(thisRef: T, desc: PropertyMetadata): List<V>
      = lazy.get { ids.map { id -> thisRef.findView(id) as V? }.filterNotNull() }
}

private class Lazy<V> {
  private object EMPTY
  private var value: Any? = EMPTY

  fun get(initializer: () -> V): V {
    if (value == EMPTY) {
      value = initializer.invoke()
    }
    @suppress("UNCHECKED_CAST")
    return value as V
  }
}
