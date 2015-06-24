package butterknife

import android.app.Activity
import android.app.Dialog
import android.app.Fragment
import android.support.v4.app.Fragment as SupportFragment
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.View
import kotlin.properties.ReadOnlyProperty

public fun <V : View> View.bindView(id: Int)
    : ReadOnlyProperty<View, V> = ViewBinding(id)
public fun <V : View> Activity.bindView(id: Int)
    : ReadOnlyProperty<Activity, V> = ViewBinding(id)
public fun <V : View> Dialog.bindView(id: Int)
    : ReadOnlyProperty<Dialog, V> = ViewBinding(id)
public fun <V : View> Fragment.bindView(id: Int)
    : ReadOnlyProperty<Fragment, V> = ViewBinding(id)
public fun <V : View> SupportFragment.bindView(id: Int)
    : ReadOnlyProperty<SupportFragment, V> = ViewBinding(id)
public fun <V : View> ViewHolder.bindView(id: Int)
    : ReadOnlyProperty<ViewHolder, V> = ViewBinding(id)

public fun <V : View> View.bindOptionalView(id: Int)
    : ReadOnlyProperty<View, V?> = OptionalViewBinding(id)
public fun <V : View> Activity.bindOptionalView(id: Int)
    : ReadOnlyProperty<Activity, V?> = OptionalViewBinding(id)
public fun <V : View> Dialog.bindOptionalView(id: Int)
    : ReadOnlyProperty<Dialog, V?> = OptionalViewBinding(id)
public fun <V : View> Fragment.bindOptionalView(id: Int)
    : ReadOnlyProperty<Fragment, V?> = OptionalViewBinding(id)
public fun <V : View> SupportFragment.bindOptionalView(id: Int)
    : ReadOnlyProperty<SupportFragment, V?> = OptionalViewBinding(id)
public fun <V : View> ViewHolder.bindOptionalView(id: Int)
    : ReadOnlyProperty<ViewHolder, V?> = OptionalViewBinding(id)

public fun <V : View> View.bindViews(vararg ids: Int)
    : ReadOnlyProperty<View, List<V>> = ViewListBinding(ids)
public fun <V : View> Activity.bindViews(vararg ids: Int)
    : ReadOnlyProperty<Activity, List<V>> = ViewListBinding(ids)
public fun <V : View> Dialog.bindViews(vararg ids: Int)
    : ReadOnlyProperty<Dialog, List<V>> = ViewListBinding(ids)
public fun <V : View> Fragment.bindViews(vararg ids: Int)
    : ReadOnlyProperty<Fragment, List<V>> = ViewListBinding(ids)
public fun <V : View> SupportFragment.bindViews(vararg ids: Int)
    : ReadOnlyProperty<SupportFragment, List<V>> = ViewListBinding(ids)
public fun <V : View> ViewHolder.bindViews(vararg ids: Int)
    : ReadOnlyProperty<ViewHolder, List<V>> = ViewListBinding(ids)

public fun <V : View> View.bindOptionalViews(vararg ids: Int)
    : ReadOnlyProperty<View, List<V>> = OptionalViewListBinding(ids)
public fun <V : View> Activity.bindOptionalViews(vararg ids: Int)
    : ReadOnlyProperty<Activity, List<V>> = OptionalViewListBinding(ids)
public fun <V : View> Dialog.bindOptionalViews(vararg ids: Int)
    : ReadOnlyProperty<Dialog, List<V>> = OptionalViewListBinding(ids)
public fun <V : View> Fragment.bindOptionalViews(vararg ids: Int)
    : ReadOnlyProperty<Fragment, List<V>> = OptionalViewListBinding(ids)
public fun <V : View> SupportFragment.bindOptionalViews(vararg ids: Int)
    : ReadOnlyProperty<SupportFragment, List<V>> = OptionalViewListBinding(ids)
public fun <V : View> ViewHolder.bindOptionalViews(vararg ids: Int)
    : ReadOnlyProperty<ViewHolder, List<V>> = OptionalViewListBinding(ids)

private fun findView<V : View>(thisRef: Any, id: Int): V? {
  @suppress("UNCHECKED_CAST")
  return when (thisRef) {
    is View -> thisRef.findViewById(id)
    is Activity -> thisRef.findViewById(id)
    is Dialog -> thisRef.findViewById(id)
    is Fragment -> thisRef.getView().findViewById(id)
    is SupportFragment -> thisRef.getView().findViewById(id)
    is ViewHolder -> thisRef.itemView.findViewById(id)
    else -> throw IllegalStateException("Unable to find views on type.")
  } as V?
}

private class ViewBinding<V : View>(val id: Int) : ReadOnlyProperty<Any, V> {
  private val lazy = Lazy<V>()

  override fun get(thisRef: Any, desc: PropertyMetadata): V = lazy.get {
    findView<V>(thisRef, id)
        ?: throw IllegalStateException("View ID $id for '${desc.name}' not found.")
  }
}

private class OptionalViewBinding<V : View>(val id: Int) : ReadOnlyProperty<Any, V?> {
  private val lazy = Lazy<V?>()

  override fun get(thisRef: Any, desc: PropertyMetadata): V? = lazy.get {
    findView<V>(thisRef, id)
  }
}

private class ViewListBinding<V : View>(val ids: IntArray) : ReadOnlyProperty<Any, List<V>> {
  private var lazy = Lazy<List<V>>()

  override fun get(thisRef: Any, desc: PropertyMetadata): List<V> = lazy.get {
    ids.map { id -> findView<V>(thisRef, id)
        ?: throw IllegalStateException("View ID $id for '${desc.name}' not found.")
    }
  }
}

private class OptionalViewListBinding<V : View>(val ids: IntArray) : ReadOnlyProperty<Any, List<V>> {
  private var lazy = Lazy<List<V>>()

  override fun get(thisRef: Any, desc: PropertyMetadata): List<V> = lazy.get {
    ids.map { id -> findView<V>(thisRef, id) }.filterNotNull()
  }
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
