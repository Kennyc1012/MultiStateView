package com.kennyc.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.IntDef
import androidx.annotation.LayoutRes
import androidx.annotation.Nullable
import com.kennyc.multistateview.R

class MultiStateView
@JvmOverloads constructor(context: Context,
                          attrs: AttributeSet? = null,
                          defStyle: Int = 0) : FrameLayout(context, attrs, defStyle) {


    companion object {
        const val VIEW_STATE_CONTENT = 0
        const val VIEW_STATE_ERROR = 1
        const val VIEW_STATE_EMPTY = 2
        const val VIEW_STATE_LOADING = 3
    }

    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @IntDef(VIEW_STATE_CONTENT, VIEW_STATE_ERROR, VIEW_STATE_EMPTY, VIEW_STATE_LOADING)
    annotation class ViewState

    private var contentView: View? = null

    private var loadingView: View? = null

    private var errorView: View? = null

    private var emptyView: View? = null

    var listener: StateListener? = null

    var animateLayoutChanges: Boolean = false

    @ViewState
    var viewState: Int = VIEW_STATE_CONTENT
        set(value) {
            val previousField = field

            if (value != previousField) {
                field = value
                setView(previousField)
                listener?.onStateChanged(value)
            }
        }

    init {
        val inflater = LayoutInflater.from(getContext())
        val a = getContext().obtainStyledAttributes(attrs, R.styleable.MultiStateView)

        val loadingViewResId = a.getResourceId(R.styleable.MultiStateView_msv_loadingView, -1)
        if (loadingViewResId > -1) {
            val inflatedLoadingView = inflater.inflate(loadingViewResId, this, false)
            loadingView = inflatedLoadingView
            addView(inflatedLoadingView, inflatedLoadingView.layoutParams)
        }

        val emptyViewResId = a.getResourceId(R.styleable.MultiStateView_msv_emptyView, -1)
        if (emptyViewResId > -1) {
            val inflatedEmptyView = inflater.inflate(emptyViewResId, this, false)
            emptyView = inflatedEmptyView
            addView(inflatedEmptyView, inflatedEmptyView.layoutParams)
        }

        val errorViewResId = a.getResourceId(R.styleable.MultiStateView_msv_errorView, -1)
        if (errorViewResId > -1) {
            val inflatedErrorView = inflater.inflate(errorViewResId, this, false)
            errorView = inflatedErrorView
            addView(inflatedErrorView, inflatedErrorView.layoutParams)
        }

        viewState = a.getInt(R.styleable.MultiStateView_msv_viewState, VIEW_STATE_CONTENT)
        animateLayoutChanges = a.getBoolean(R.styleable.MultiStateView_msv_animateViewChanges, false)
        a.recycle()
    }

    /**
     * Returns the [View] associated with the [com.kennyc.view.MultiStateView.ViewState]
     *
     * @param state The [com.kennyc.view.MultiStateView.ViewState] with to return the view for
     * @return The [View] associated with the [com.kennyc.view.MultiStateView.ViewState], null if no view is present
     */
    @Nullable
    fun getView(@ViewState state: Int): View? {
        return when (state) {
            VIEW_STATE_LOADING -> loadingView

            VIEW_STATE_CONTENT -> contentView

            VIEW_STATE_EMPTY -> emptyView

            VIEW_STATE_ERROR -> errorView

            else -> throw IllegalArgumentException("Unknown ViewState $state")
        }
    }

    /**
     * Sets the view for the given view state
     *
     * @param view          The [View] to use
     * @param state         The [com.kennyc.view.MultiStateView.ViewState]to set
     * @param switchToState If the [com.kennyc.view.MultiStateView.ViewState] should be switched to
     */
    fun setViewForState(view: View, @ViewState state: Int, switchToState: Boolean = false) {
        when (state) {
            VIEW_STATE_LOADING -> {
                if (loadingView != null) removeView(loadingView)
                loadingView = view
                addView(view)
            }

            VIEW_STATE_EMPTY -> {
                if (emptyView != null) removeView(emptyView)
                emptyView = view
                addView(view)
            }

            VIEW_STATE_ERROR -> {
                if (errorView != null) removeView(errorView)
                errorView = view
                addView(view)
            }

            VIEW_STATE_CONTENT -> {
                if (contentView != null) removeView(contentView)
                contentView = view
                addView(view)
            }

            else -> throw IllegalArgumentException("Unable to set view for state $state")
        }

        if (switchToState) viewState = state
    }

    /**
     * Sets the [View] for the given [com.kennyc.view.MultiStateView.ViewState]
     *
     * @param layoutRes     Layout resource id
     * @param state         The [com.kennyc.view.MultiStateView.ViewState] to set
     * @param switchToState If the [com.kennyc.view.MultiStateView.ViewState] should be switched to
     */
    fun setViewForState(@LayoutRes layoutRes: Int, @ViewState state: Int, switchToState: Boolean = false) {
        val view = LayoutInflater.from(context).inflate(layoutRes, this, false)
        setViewForState(view, state, switchToState)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (contentView == null) throw IllegalArgumentException("Content view is not defined")

        when (viewState) {
            VIEW_STATE_CONTENT -> setView(VIEW_STATE_CONTENT)
            else -> contentView?.visibility = View.GONE
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        return when (val superState = super.onSaveInstanceState()) {
            null -> superState
            else -> SavedState(superState, viewState)
        }
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        super.onRestoreInstanceState(state)
        if (state is SavedState) viewState = state.state
    }

    /* All of the addView methods have been overridden so that it can obtain the content view via XML
     It is NOT recommended to add views into MultiStateView via the addView methods, but rather use
     any of the setViewForState methods to set views for their given ViewState accordingly */
    override fun addView(child: View) {
        if (isValidContentView(child)) contentView = child
        super.addView(child)
    }

    override fun addView(child: View, index: Int) {
        if (isValidContentView(child)) contentView = child
        super.addView(child, index)
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        if (isValidContentView(child)) contentView = child
        super.addView(child, index, params)
    }

    override fun addView(child: View, params: ViewGroup.LayoutParams) {
        if (isValidContentView(child)) contentView = child
        super.addView(child, params)
    }

    override fun addView(child: View, width: Int, height: Int) {
        if (isValidContentView(child)) contentView = child
        super.addView(child, width, height)
    }

    override fun addViewInLayout(child: View, index: Int, params: ViewGroup.LayoutParams): Boolean {
        if (isValidContentView(child)) contentView = child
        return super.addViewInLayout(child, index, params)
    }

    override fun addViewInLayout(child: View, index: Int, params: ViewGroup.LayoutParams, preventRequestLayout: Boolean): Boolean {
        if (isValidContentView(child)) contentView = child
        return super.addViewInLayout(child, index, params, preventRequestLayout)
    }

    /**
     * Checks if the given [View] is valid for the Content View
     *
     * @param view The [View] to check
     * @return
     */
    private fun isValidContentView(view: View): Boolean {
        return if (contentView != null && contentView !== view) {
            false
        } else view != loadingView && view != errorView && view != emptyView
    }

    /**
     * Shows the [View] based on the [com.kennyc.view.MultiStateView.ViewState]
     */
    private fun setView(@ViewState previousState: Int) {
        when (viewState) {
            VIEW_STATE_LOADING -> {
                requireNotNull(loadingView).apply {
                    contentView?.visibility = View.GONE
                    errorView?.visibility = View.GONE
                    emptyView?.visibility = View.GONE

                    if (animateLayoutChanges) {
                        animateLayoutChange(getView(previousState))
                    } else {
                        visibility = View.VISIBLE
                    }
                }
            }

            VIEW_STATE_EMPTY -> {
                requireNotNull(emptyView).apply {
                    contentView?.visibility = View.GONE
                    errorView?.visibility = View.GONE
                    loadingView?.visibility = View.GONE

                    if (animateLayoutChanges) {
                        animateLayoutChange(getView(previousState))
                    } else {
                        visibility = View.VISIBLE
                    }
                }
            }

            VIEW_STATE_ERROR -> {
                requireNotNull(errorView).apply {
                    contentView?.visibility = View.GONE
                    loadingView?.visibility = View.GONE
                    emptyView?.visibility = View.GONE

                    if (animateLayoutChanges) {
                        animateLayoutChange(getView(previousState))
                    } else {
                        visibility = View.VISIBLE
                    }
                }
            }

            VIEW_STATE_CONTENT -> {
                requireNotNull(contentView).apply {
                    loadingView?.visibility = View.GONE
                    errorView?.visibility = View.GONE
                    emptyView?.visibility = View.GONE

                    if (animateLayoutChanges) {
                        animateLayoutChange(getView(previousState))
                    } else {
                        visibility = View.VISIBLE
                    }
                }
            }

            else -> {
                throw IllegalArgumentException("Unable to set state for value $viewState")
            }
        }
    }

    /**
     * Animates the layout changes between [ViewState]
     *
     * @param previousView The view that it was currently on
     */
    private fun animateLayoutChange(@Nullable previousView: View?) {
        if (previousView == null) {
            requireNotNull(getView(viewState)).visibility = View.VISIBLE
            return
        }

        ObjectAnimator.ofFloat(previousView, "alpha", 1.0f, 0.0f).apply {
            duration = 250L
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    previousView.visibility = View.VISIBLE
                }

                override fun onAnimationEnd(animation: Animator) {
                    previousView.visibility = View.GONE
                    val currentView = requireNotNull(getView(viewState))
                    currentView.visibility = View.VISIBLE
                    ObjectAnimator.ofFloat(currentView, "alpha", 0.0f, 1.0f).setDuration(250L).start()
                }
            })
        }.start()
    }

    interface StateListener {
        /**
         * Callback for when the [ViewState] has changed
         *
         * @param viewState The [ViewState] that was switched to
         */
        fun onStateChanged(@ViewState viewState: Int)
    }

    private class SavedState : BaseSavedState {
        internal val state: Int

        constructor(superState: Parcelable, state: Int) : super(superState) {
            this.state = state
        }

        constructor(parcel: Parcel) : super(parcel) {
            state = parcel.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(state)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(`in`: Parcel): SavedState {
                    return SavedState(`in`)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }
}