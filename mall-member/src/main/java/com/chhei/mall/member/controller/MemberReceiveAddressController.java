package com.chhei.mall.member.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.chhei.mall.member.entity.MemberReceiveAddressEntity;
import com.chhei.mall.member.service.MemberReceiveAddressService;
import com.chhei.common.utils.PageUtils;
import com.chhei.common.utils.R;



/**
 * 会员收货地址
 *
 * @author chhei
 * @email 1835494827@qq.com
 * @date 2024-09-14 14:10:04
 */
@RestController
@RequestMapping("member/memberreceiveaddress")
public class MemberReceiveAddressController {
    @Autowired
    private MemberReceiveAddressService memberReceiveAddressService;

    @GetMapping("/{memberId}/address")
    public List<MemberReceiveAddressEntity> getAddress(@PathVariable("memberId") Long memberId) {


        return memberReceiveAddressService.getAddress(memberId);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("member:memberreceiveaddress:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberReceiveAddressService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("member:memberreceiveaddress:info")
    public R info(@PathVariable("id") Long id){
		MemberReceiveAddressEntity memberReceiveAddress = memberReceiveAddressService.getById(id);

        return R.ok().put("memberReceiveAddress", memberReceiveAddress);
    }

    @RequestMapping({"/getAddressById/{id}"})
    public MemberReceiveAddressEntity getAddressById(@PathVariable("id") Long id) {
        MemberReceiveAddressEntity memberReceiveAddress = (MemberReceiveAddressEntity)this.memberReceiveAddressService.getById(id);
        return memberReceiveAddress;
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("member:memberreceiveaddress:save")
    public R save(@RequestBody MemberReceiveAddressEntity memberReceiveAddress){
		memberReceiveAddressService.save(memberReceiveAddress);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("member:memberreceiveaddress:update")
    public R update(@RequestBody MemberReceiveAddressEntity memberReceiveAddress){
		memberReceiveAddressService.updateById(memberReceiveAddress);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("member:memberreceiveaddress:delete")
    public R delete(@RequestBody Long[] ids){
		memberReceiveAddressService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
