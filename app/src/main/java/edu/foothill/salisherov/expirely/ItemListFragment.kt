package edu.foothill.salisherov.expirely

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import java.util.*


private const val EXPIRED_TAG = "Already expired..."

class ItemListFragment : Fragment() {
    interface Callbacks {
        fun onItemSelected(itemId: UUID)
    }

    private var callbacks: Callbacks? = null
    private lateinit var itemRecyclerView: RecyclerView
    private lateinit var deleteItem: ImageButton
    private var adapter: ItemAdapter? = ItemAdapter(emptyList())
    private var tracker: SelectionTracker<Long>? = null

    private val itemListViewModel: ItemListViewModel by lazy {
        ViewModelProviders.of(this).get(ItemListViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    companion object {
        fun newInstance(): ItemListFragment {
            return ItemListFragment()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        itemRecyclerView =
            view.findViewById(R.id.item_recycler_view) as RecyclerView
        itemRecyclerView.layoutManager = LinearLayoutManager(context)
        itemRecyclerView.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemListViewModel.itemListLiveData.observe(
            viewLifecycleOwner,
            Observer { items ->
                items?.let {
                    updateUI(items)
                }
            })
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_item_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_item -> {
                val item = Item(UUID.randomUUID(), "", Date(), false, 0, false)
                itemListViewModel.addItem(item)
                callbacks?.onItemSelected(item.id)
                true
            }
            R.id.action_delete ->{
                Toast.makeText(context, "All items deletion isn't operational", Toast.LENGTH_LONG)
                    .show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun updateUI(items: List<Item>) {
        adapter = ItemAdapter(items)
        itemRecyclerView.adapter = adapter
    }

    private inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        private lateinit var item: Item

        private val nameTextView: TextView = itemView.findViewById(R.id.item_name)
        private val dateTextView: TextView = itemView.findViewById(R.id.item_date)
        private val daysTextView: TextView = itemView.findViewById(R.id.item_days)


        init {
            itemView.setOnClickListener(this)


            deleteItem = view.findViewById(R.id.deleteItem) as ImageButton
            deleteItem.setOnClickListener {

                val snack =
                    Snackbar.make(it, "Do you really want to delete it?", Snackbar.LENGTH_LONG)
                snack.setAction("Yes", actionBarDeletion)
                context?.let { it1 -> ContextCompat.getColor(it1, R.color.back_red) }?.let { it2 ->
                    snack.setActionTextColor(
                        it2
                    )
                }
                snack.show()
            }
        }

        var actionBarDeletion: View.OnClickListener = View.OnClickListener { view ->
            adapter?.notifyDataSetChanged()
            itemListViewModel.deleteItem(item)
            Snackbar.make(view, "Item removed", Snackbar.LENGTH_LONG).show()
        }

        fun bind(item: Item) {
            this.item = item
            val milliSecond = 86400000.0f
            var days = ((this.item.date.time - Date().time) / milliSecond).toInt()
            var stringDate = this.item.date.toString()
            var dateCut = stringDate.slice(0..10) + stringDate.slice(24..27)

            nameTextView.text = this.item.name
            dateTextView.text = dateCut
            if (days > 0) {
                if (days == 1) {
                    itemView.setBackgroundResource(R.drawable.back_yellow)
                    daysTextView.text = "$days day left..."
                } else {
                    itemView.setBackgroundResource(R.drawable.back_green)
                    daysTextView.text = "$days days left..."
                }
            } else {
                item.expired = true
                itemView.setBackgroundResource(R.drawable.back_red)
                daysTextView.text = EXPIRED_TAG
            }

        }

        override fun onClick(v: View) {
            callbacks?.onItemSelected(item.id)
        }
    }

    private inner class ItemAdapter(var items: List<Item>) : RecyclerView.Adapter<ItemHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : ItemHolder {
            val view = layoutInflater.inflate(R.layout.list_item, parent, false)
            return ItemHolder(view)
        }

        override fun getItemCount() = items.size

        override fun onBindViewHolder(holder: ItemHolder, position: Int) {
            val item = items[position]
            holder.bind(item)
        }
    }

}