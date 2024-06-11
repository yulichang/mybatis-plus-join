package com.github.yulichang.adapter;

import com.baomidou.mybatisplus.core.MybatisPlusVersion;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.github.yulichang.adapter.base.IAdapter;
import com.github.yulichang.adapter.base.tookit.VersionUtils;
import com.github.yulichang.adapter.v33x.AdapterV33x;
import com.github.yulichang.adapter.v3431.Adapter3431;
import com.github.yulichang.adapter.v355.Adapter355;
import lombok.Getter;

import java.util.Optional;

/**
 * @author yulichang
 * @since 1.4.3
 */
public class AdapterHelper {

    @Getter
    private static IAdapter adapter;


    static {
        String lastAdapter = "3.5.7";
        String version = Optional.of(MybatisPlusVersion.getVersion()).orElse(lastAdapter);

        if (VersionUtils.compare(version, lastAdapter) >= 0) {
            adapter = new Adapter();
        } else if (VersionUtils.compare(version, "3.5.4") >= 0) {
            adapter = new Adapter355();
        } else if (VersionUtils.compare(version, "3.4.0") >= 0) {
            adapter = new Adapter3431();
        } else if (VersionUtils.compare(version, "3.3.0") >= 0) {
            adapter = new AdapterV33x();
        } else {
            throw ExceptionUtils.mpe("MPJ需要MP版本3.3.0+，当前MP版本%s", version);
        }
    }

    public static void setAdapter(IAdapter adapter) {
        AdapterHelper.adapter = adapter;
    }
}
