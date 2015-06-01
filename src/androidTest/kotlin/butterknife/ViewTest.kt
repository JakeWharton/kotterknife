package butterknife

import android.content.Context
import android.widget.FrameLayout
import android.widget.TextView
import android.test.AndroidTestCase
import android.view.View
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertEquals

public class ViewTest : AndroidTestCase() {
  public fun testCast() {
    class Example(context: Context) : FrameLayout(context) {
      val name : TextView by bindView(1)
    }

    val example = Example(getContext())
    example.addView(textViewWithId(1))
    assertNotNull(example.name)
  }

  public fun testFindCached() {
    class Example(context: Context) : FrameLayout(context) {
      val name : View by bindView(1)
    }

    val example = Example(getContext())
    example.addView(viewWithId(1))
    assertNotNull(example.name)
    example.removeAllViews()
    assertNotNull(example.name)
  }

  public fun testOptional() {
    class Example(context: Context) : FrameLayout(context) {
      val present: View? by bindOptionalView(1)
      val missing: View? by bindOptionalView(2)
    }

    val example = Example(getContext())
    example.addView(viewWithId(1))
    assertNotNull(example.present)
    assertNull(example.missing)
  }

  public fun testOptionalCached() {
    class Example(context: Context) : FrameLayout(context) {
      val present: View? by bindOptionalView(1)
      val missing: View? by bindOptionalView(2)
    }

    val example = Example(getContext())
    example.addView(viewWithId(1))
    assertNotNull(example.present)
    assertNull(example.missing)
    example.removeAllViews()
    example.addView(viewWithId(2))
    assertNotNull(example.present)
    assertNull(example.missing)
  }

  public fun testMissingFails() {
    class Example(context: Context) : FrameLayout(context) {
      val name : TextView? by bindView(1)
    }

    val example = Example(getContext())
    try {
      example.name
    } catch (e: IllegalStateException) {
      assertEquals("View ID 1 for 'name' not found.", e.getMessage())
    }
  }

  public fun testList() {
    class Example(context: Context) : FrameLayout(context) {
      val name : List<TextView> by bindViews(1, 2, 3)
    }

    val example = Example(getContext())
    example.addView(viewWithId(1))
    example.addView(viewWithId(2))
    example.addView(viewWithId(3))
    assertNotNull(example.name)
    assertEquals(3, example.name.size())
  }

  public fun testListCaches() {
    class Example(context: Context) : FrameLayout(context) {
      val name : List<TextView> by bindViews(1, 2, 3)
    }

    val example = Example(getContext())
    example.addView(viewWithId(1))
    example.addView(viewWithId(2))
    example.addView(viewWithId(3))
    assertNotNull(example.name)
    assertEquals(3, example.name.size())
    example.removeAllViews()
    assertNotNull(example.name)
    assertEquals(3, example.name.size())
  }

  public fun testListMissingFails() {
    class Example(context: Context) : FrameLayout(context) {
      val name : List<TextView> by bindViews(1, 2, 3)
    }

    val example = Example(getContext())
    example.addView(viewWithId(1))
    example.addView(viewWithId(3))
    try {
      example.name
    } catch (e: IllegalStateException) {
      assertEquals("View ID 2 for 'name' not found.", e.getMessage())
    }
  }

  public fun testOptionalList() {
    class Example(context: Context) : FrameLayout(context) {
      val name : List<TextView> by bindOptionalViews(1, 2, 3)
    }

    val example = Example(getContext())
    example.addView(viewWithId(1))
    example.addView(viewWithId(3))
    assertNotNull(example.name)
    assertEquals(2, example.name.size())
  }

  public fun testOptionalListCaches() {
    class Example(context: Context) : FrameLayout(context) {
      val name : List<TextView> by bindOptionalViews(1, 2, 3)
    }

    val example = Example(getContext())
    example.addView(viewWithId(1))
    example.addView(viewWithId(3))
    assertNotNull(example.name)
    assertEquals(2, example.name.size())
    example.removeAllViews()
    assertNotNull(example.name)
    assertEquals(2, example.name.size())
  }

  private fun viewWithId(id: Int) : View {
    val view = View(getContext())
    view.setId(id)
    return view
  }

  private fun textViewWithId(id: Int) : View {
    val view = TextView(getContext())
    view.setId(id)
    return view
  }
}
