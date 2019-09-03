package cn.wj.android.recyclerview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import cn.wj.android.recyclerview.BR

/**
 * RecyclerView 适配器基类
 * - 若要使 头布局、脚布局 等必须在设置适配器之前设置布局管理器
 *
 * @param VH ViewHolder 类型，继承 [BaseRvDBViewHolder]
 * @param DB  DataBinding 类型，与 VH 一致 继承 [ViewDataBinding]
 * @param VM  事件处理类型 Model
 * @param E  数据类型
 *
 * @author 王杰
 */
abstract class BaseRvDBAdapter<out VH : BaseRvDBViewHolder<DB, E>, DB : ViewDataBinding, VM, E>
    : BaseRvAdapter<BaseRvViewHolder<E>, E>() {

    /** 事件处理  */
    var mViewModel: VM? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRvViewHolder<E> {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                // 头布局
                createViewHolder(mHeaderLayout!!)
            }
            VIEW_TYPE_FOOTER -> {
                // 脚布局
                createViewHolder(mFooterLayout!!)
            }
            VIEW_TYPE_EMPTY -> {
                // 空布局
                createViewHolder(mEmptyLayout!!)
            }
            else -> {
                customCreateViewHolder(parent, viewType)
            }
        }
    }

    /**
     * 创建ViewHolder
     *
     * @param binding DataBinding对象
     *
     * @return ViewHolder 对象
     */
    protected open fun createViewHolder(binding: DB): BaseRvDBViewHolder<DB, E> {
        @Suppress("UNCHECKED_CAST")
        val holderConstructor = (getVHClass() as Class<BaseRvDBViewHolder<DB, E>>).getConstructor(getDBClass())
        return holderConstructor.newInstance(binding)
    }

    /**
     * 创建ViewHolder 使用头布局时必须重写
     *
     * @param view View对象
     *
     * @return ViewHolder
     */
    override fun createViewHolder(view: View): BaseRvViewHolder<E> {
        @Suppress("UNCHECKED_CAST")
        val clazz = getVHClass().superclass as Class<BaseRvViewHolder<E>>
        val constructor = clazz.getConstructor(View::class.java)
        return constructor.newInstance(view)
    }

    /**
     * 除特殊布局外的其他布局，一种布局无需变化，多种布局重写
     */
    override fun customCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRvDBViewHolder<DB, E> {
        // 普通布局
        // 加载布局，初始化 DataBinding
        val binding = DataBindingUtil.inflate<DB>(
                LayoutInflater.from(parent.context),
                layoutResID, parent, false
        )
        // 绑定事件处理
        mViewModel?.let {
            binding.setVariable(BR.viewModel, mViewModel)
        }
        // 创建 ViewHolder
        return createViewHolder(binding)
    }

    /**
     * 获取 ViewHolder 的类
     *
     * @return ViewHolder 实际类型
     */
    override fun getVHClass() = getActualTypeList()[0] as Class<*>

    /**
     * 获取 DataBinding 的类
     *
     * @return DataBinding 的实际类型
     */
    @Suppress("UNCHECKED_CAST")
    protected open fun getDBClass() = getActualTypeList()[1] as Class<DB>

    /**
     * 绑定 ViewModel
     *
     * @param viewModel ViewModel 对象
     */
    fun bindViewModel(viewModel: VM) {
        mViewModel = viewModel
    }

    /** 布局 id */
    abstract val layoutResID: Int
}