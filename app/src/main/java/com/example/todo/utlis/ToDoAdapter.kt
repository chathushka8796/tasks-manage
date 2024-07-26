/*package com.example.todo.utlis

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.databinding.EachTodoItemBinding

class ToDoAdapter(private val list:MutableList<ToDoData>):
RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>(){
    private var listner:ToDoAdpterClicksInterface?=null
    fun setListener(listener:ToDoAdpterClicksInterface){
        this.listner = listner
    }
    inner class ToDoViewHolder(binding:EachTodoItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val binding = EachTodoItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ToDoViewHolder(binding)
    }

    override fun getItemCount(): Int {
      return  list.size
    }
    interface ToDoAdpterClicksInterface {
        fun onDeleteTaskBtnClicked(toDoData: ToDoData)
        fun onEditTaskBtnClicked(toDoData: ToDoData)
        
    }
    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        with(holder){
            with(list[position]){
                binding.todoTask.text = this.task
                binding.deleteTask.setOnClickListener{
                    listner?.onDeleteTaskBtnClicked(this)

                }
                binding.editTask.setOnClickListener{
                    listner?.onEditTaskBtnClicked(this)
                    
                }

            }
        }
    }



    }

}*/
package com.example.todo.utlis

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.databinding.EachTodoItemBinding

class ToDoAdapter(private val list: MutableList<ToDoData>) :
    RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {

    private var listener: ToDoAdapterClicksInterface? = null

    fun setListener(listener: ToDoAdapterClicksInterface) {
        this.listener = listener
    }

    inner class ToDoViewHolder(val binding: EachTodoItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val binding = EachTodoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ToDoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        with(holder) {
            with(list[position]) {
                binding.todoTask.text = this.task
                binding.deleteTask.setOnClickListener {
                    listener?.onDeleteTaskBtnClicked(this)
                }
                binding.editTask.setOnClickListener {
                    listener?.onEditTaskBtnClicked(this)
                }
            }
        }
    }

    interface ToDoAdapterClicksInterface {
        fun onDeleteTaskBtnClicked(toDoData: ToDoData)
        fun onEditTaskBtnClicked(toDoData: ToDoData)
    }
}
