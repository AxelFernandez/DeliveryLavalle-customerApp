package com.axelfernandez.deliverylavalle.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.axelfernandez.deliverylavalle.R
import com.axelfernandez.deliverylavalle.adapters.CompanyAdapter
import com.axelfernandez.deliverylavalle.adapters.CompanyCategotyAdapter
import com.axelfernandez.deliverylavalle.models.CompanyCategoryResponse
import com.facebook.shimmer.ShimmerFrameLayout
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.app_bar.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.shimer_company.view.*
import java.time.Duration


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var root: View
    lateinit var categoryRv : RecyclerView
    lateinit var companyRv : RecyclerView
    lateinit var categoriesAdapter : CompanyCategotyAdapter
    val companyAdapter : CompanyAdapter = CompanyAdapter()
    var accessToken : String = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)

        root = inflater.inflate(R.layout.fragment_home, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val banner_title: TextView = root.findViewById(R.id.banner_title)
        val banner_subtitle: TextView = root.findViewById(R.id.banner_subtitle)
        val banner_image: ImageView = root.findViewById(R.id.banner_image)
        categoryRv = root.findViewById(R.id.company_category_rv) as RecyclerView
        companyRv = root.findViewById(R.id.company_rv) as RecyclerView
        root.app_bar_1.text = "Comercios "
        root.app_bar_2.text = "cerca de tu zona"
        val loadingCategories = root.findViewById(R.id.shimmer_view_container) as ShimmerFrameLayout
        val loadingCompany = root.findViewById(R.id.shimmer_company) as ShimmerFrameLayout
        loadingCategories.startShimmer()

        homeViewModel.banner_title_vm.observe(viewLifecycleOwner, Observer {
            banner_title.text = it
        })
        homeViewModel.banner_subtitle_vm.observe(viewLifecycleOwner, Observer {
            banner_subtitle.text = it
        })
        homeViewModel.banner_image_vm.observe(viewLifecycleOwner, Observer {
            Picasso.with(context).load(it).into(banner_image)
        })
        var hasBeenCalled : Boolean = false

        homeViewModel.returnToken().observe(viewLifecycleOwner, Observer {
            if (!hasBeenCalled) {
                homeViewModel.getCategoty(it.access_token)
                homeViewModel.getLocationAndGetCompany(requireActivity(), it.access_token,null)
                accessToken = it.access_token
                hasBeenCalled = true
            }
        })
        homeViewModel.returnCompany().observe(viewLifecycleOwner, Observer{
            val animation: LayoutAnimationController =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_anim_fall_down)
            companyRv.layoutAnimation = animation
            companyRv.setHasFixedSize(true)
            companyRv.layoutManager = LinearLayoutManager(requireContext())
            companyAdapter.CompanyAdapter(requireContext(), it)
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
            categoryRv.setHasFixedSize(true)
            categoryRv.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            categoriesAdapter = CompanyCategotyAdapter(it,requireContext()) {onClickActionItemList(it)}
            categoryRv.adapter = categoriesAdapter
            loadingCategories.visibility = View.GONE


        })


        homeViewModel.initial(requireContext())
    }
    fun onClickActionItemList(item: CompanyCategoryResponse) {
        root.text_view_feed_company.text = getString(R.string.company_near_category).format(item.description)
        homeViewModel.getLocationAndGetCompany(requireActivity(), accessToken, item.description)
        root.shimmer_company.visibility = View.VISIBLE
        companyRv.visibility = View.GONE
        root.no_company_found_feed.visibility = GONE
        companyAdapter.notifyDataSetChanged()

    }


}