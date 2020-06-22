package com.qhit.fsaas.util;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class Init implements ApplicationRunner {
    @Resource
    private MainUtil mainUtil;

    @Override
    public void run(ApplicationArguments args) {
//        System.out.println(
//                "                                  _oo8oo_\n" +
//                "                                 o8888888o\n" +
//                "                                 88\" . \"88\n" +
//                "                                 (| -_- |)\n" +
//                "                                 0\\  =  /0\n" +
//                "                               ___/'==='\\___\n" +
//                "                             .' \\\\|     |// '.\n" +
//                "                            / \\\\|||  :  |||// \\\n" +
//                "                           / _||||| -:- |||||_ \\\n" +
//                "                          |   | \\\\\\  -  /// |   |\n" +
//                "                          | \\_|  ''\\---/''  |_/ |\n" +
//                "                          \\  .-\\__  '-'  __/-.  /\n" +
//                "                        ___'. .'  /--.--\\  '. .'___\n" +
//                "                     .\"\" '<  '.___\\_<|>_/___.'  >' \"\".\n" +
//                "                    | | :  `- \\`.:`\\ _ /`:.`/ -`  : | |\n" +
//                "                    \\  \\ `-.   \\_ __\\ /__ _/   .-` /  /\n" +
//                "                =====`-.____`.___ \\_____/ ___.`____.-`=====\n" +
//                "                                  `=---=`\n" +
//                "              ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n" +
//                "                         佛祖保佑        永无BUG");
        mainUtil.init();
        mainUtil.showSeatCount();
        System.out.println("\n------------INIT FINISH!------------");
    }
}
