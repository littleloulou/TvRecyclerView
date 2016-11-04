package com.lp.recyclerview4tv.data;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lph on 2016/11/3.
 * 模拟数据，如果想扩展可以使用RxJava 和 Retrofit 从网络获取资源
 */
public class Datas {
    public static List<String> getDatas() {
        String[] datas = {
                "http://img4.imgtn.bdimg.com/it/u=2263564587,3827516789&fm=11&gp=0.jpg",
                "http://img4.imgtn.bdimg.com/it/u=2001849689,2934143493&fm=11&gp=0.jpg",
                "http://img1.imgtn.bdimg.com/it/u=3757412355,3884139848&fm=21&gp=0.jpg",
                "http://img3.imgtn.bdimg.com/it/u=419977589,4263339770&fm=11&gp=0.jpg",
                "http://img4.imgtn.bdimg.com/it/u=3059488398,1048400338&fm=21&gp=0.jpg",
                "http://img3.imgtn.bdimg.com/it/u=1560600784,4035223257&fm=21&gp=0.jpg",
                "http://img3.imgtn.bdimg.com/it/u=2497352402,3816602579&fm=21&gp=0.jpg",
                "http://d.ifengimg.com/w155_h107_q80/p0.ifengimg.com/a/2016_45/293f360c3994b17_size66_w206_h142.jpg",
                "http://d.ifengimg.com/w512_h288_q80/p3.ifengimg.com/cmpp/2016/11/03/502bdc446946330749bd670e73252746_size298_w640_h360.jpg",
                "http://d.ifengimg.com/w132_h94_q80/p0.ifengimg.com/a/2016_45/be27daf8bd89165_size64_w554_h361.jpg",
                "https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=412147864,1332891885&fm=80&w=179&h=119&img.jpg",
                "http://d.ifengimg.com/w132_h94_q80/p0.ifengimg.com/ifengimcp/pic/20161103/199466c84c143bc76a61_size9_w168_h120.jpg",
                "http://img1.imgtn.bdimg.com/it/u=1207158778,2343027651&fm=11&gp=0.jpg",
                "http://d.hiphotos.baidu.com/image/h%3D360/sign=856d60650933874483c5297a610fd937/55e736d12f2eb938e81944c7d0628535e5dd6f8a.jpg",
                "http://d.ifengimg.com/w166_h120/p0.ifengimg.com/ifengiclient/ipic/2016052821/swoole_location_0b913b85734fa084386466b74facd719_size96_w533_h768.jpg"
        };
        return Arrays.asList(datas);
    }
}
