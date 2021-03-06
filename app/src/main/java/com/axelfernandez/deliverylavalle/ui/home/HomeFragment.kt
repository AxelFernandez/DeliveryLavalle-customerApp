package com.axelfernandez.deliverylavalle.ui.home

import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.axelfernandez.deliverylavalle.OrderActivity
import com.axelfernandez.deliverylavalle.R
import com.axelfernandez.deliverylavalle.adapters.CompanyAdapter
import com.axelfernandez.deliverylavalle.adapters.CompanyCategotyAdapter
import com.axelfernandez.deliverylavalle.models.Company
import com.axelfernandez.deliverylavalle.models.CompanyCategoryResponse
import com.axelfernandez.deliverylavalle.models.User
import com.axelfernandez.deliverylavalle.utils.LoginUtils
import com.axelfernandez.deliverylavalle.utils.ViewUtil
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.location.LocationListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.app_bar.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.no_company_found.view.*
import kotlinx.android.synthetic.main.shimer_company.view.*
import java.util.concurrent.atomic.AtomicBoolean


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var root: View
    lateinit var categoryRv : RecyclerView
    lateinit var companyRv : RecyclerView
    lateinit var categoriesAdapter : CompanyCategotyAdapter
    lateinit var companyAdapter : CompanyAdapter

    companion object {
        fun newInstance() =
            HomeFragment()
        var atomicBoolean = AtomicBoolean(false)

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        homeViewModel.getRepository(requireContext())
        root = inflater.inflate(R.layout.fragment_home, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val bannerTitle: TextView = root.findViewById(R.id.banner_title)
        val bannerSubtitle: TextView = root.findViewById(R.id.banner_subtitle)
        val bannerImage: ImageView = root.findViewById(R.id.banner_image)
        val view = view?:return
        categoryRv = root.findViewById(R.id.company_category_rv) as RecyclerView
        companyRv = root.findViewById(R.id.company_rv) as RecyclerView
        root.app_bar_1.text = "Comercios "
        root.app_bar_2.text = "cerca de tu zona"
        val loadingCategories = root.findViewById(R.id.shimmer_view_container) as ShimmerFrameLayout
        val loadingCompany = root.findViewById(R.id.shimmer_company) as ShimmerFrameLayout
        loadingCategories.startShimmer()
        root.not_found_title.text = getString(R.string.not_found_title_company)
        root.not_found_subtitle.text = getString(R.string.not_found_subtitle_company)

        homeViewModel.banner_title_vm.observe(viewLifecycleOwner, Observer {
            bannerTitle.text = it
        })
        homeViewModel.banner_subtitle_vm.observe(viewLifecycleOwner, Observer {
            bannerSubtitle.text = it
        })
        homeViewModel.banner_image_vm.observe(viewLifecycleOwner, Observer {
            Picasso.with(context).load(it).placeholder(requireContext().getDrawable(R.drawable.ic_abstract)).into(bannerImage)
        })

        homeViewModel.getCategoty()

        homeViewModel.returnCompany().observe(viewLifecycleOwner, Observer{
            if(it == null){
                ViewUtil.setSnackBar(root, R.color.red,getString(R.string.no_conection))
                return@Observer
            }
            val animation: LayoutAnimationController =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_anim_fall_down)
            companyRv.layoutAnimation = animation
            companyRv.setHasFixedSize(true)
            companyRv.layoutManager = LinearLayoutManager(requireContext())
            companyAdapter = CompanyAdapter(it, requireContext()){companyOnClickListener(it)}
            companyRv.adapter = companyAdapter
            if(it.isEmpty()) {
                root.no_company_found_feed.visibility = VISIBLE
            }else{
                root.no_company_found_feed.visibility = GONE

            }
            loadingCompany.visibility = View.GONE
            companyRv.visibility = View.VISIBLE

        })

        homeViewModel.returnCategory().observe(viewLifecycleOwner, Observer {
            if(it == null){
                ViewUtil.setSnackBar(root,R.color.red,getString(R.string.no_conection))
            }
            val it = it?:return@Observer
            categoryRv.setHasFixedSize(true)
            categoryRv.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            categoriesAdapter = CompanyCategotyAdapter(it,requireContext()) {onClickActionItemList(it)}
            categoryRv.adapter = categoriesAdapter
            loadingCategories.visibility = View.GONE


        })
        homeViewModel.createLocationRequest()
    }
    fun onClickActionItemList(item: CompanyCategoryResponse) {
        root.text_view_feed_company.text = getString(R.string.company_near_category).format(item.description)
        homeViewModel.getLocationAndGetCompany(requireActivity(), item.description,view?:return)
        root.shimmer_company.visibility = View.VISIBLE
        companyRv.visibility = View.GONE
        root.no_company_found_feed.visibility = GONE

    }

    private fun companyOnClickListener(item: Company){
        if (item.isOpen){
            val intent = Intent(context, OrderActivity::class.java)
            intent.putExtra(getString(R.string.company_id),item.id)
            startActivity(intent)
        }else{
            ViewUtil.setSnackBar(view?:return, R.color.orange,"Este lugar esta cerrado, vuelve luego")
        }
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.getLocationAndGetCompany(requireActivity(), null,requireView())

    }
}