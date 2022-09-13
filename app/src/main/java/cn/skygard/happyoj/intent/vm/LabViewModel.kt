package cn.skygard.happyoj.intent.vm

import android.util.Log
import androidx.lifecycle.viewModelScope
import cn.skygard.common.mvi.ext.setState
import cn.skygard.common.mvi.vm.BaseViewModel
import cn.skygard.happyoj.intent.state.FetchState
import cn.skygard.happyoj.intent.state.LabAction
import cn.skygard.happyoj.intent.state.LabEvent
import cn.skygard.happyoj.intent.state.LabState
import cn.skygard.happyoj.domain.model.TasksItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LabViewModel(val taskItem: TasksItem) :
    BaseViewModel<LabState, LabAction, LabEvent>(LabState()) {

    override fun dispatch(action: LabAction) {
        Log.d("LabViewModel", "received an action $action")
        when (action) {
            is LabAction.FetchContent -> fetchContent(action.noCache)
        }
    }

    private fun fetchContent(noCache: Boolean) {
        Log.d("LabViewModel", "fetching data from ${taskItem.mdUrl}")
        mViewStates.setState {
            copy(fetchState = FetchState.Fetching)
        }
        viewModelScope.launch {
            delay(1000)
            mViewStates.setState {
                copy(
                    fetchState = FetchState.Fetched,
                    mdContent = """
                        # 前言
                        
                        > 如果你是iPhone，请打开你的淘宝，京东，亚马逊，~~拼多多~~ 等购物软件，输入Android手机，然后选择一款即可 
                        >
                        > 主体部分其实就是`如何搭建一个Hexo博客`，只不过全程都在使用类Linux操作系统的 `Termux` 而已
                        
                        1. 为什么要在手机上部署？
                        
                           + 没有电脑或者电脑不方便使用
                           + 没有云服务器
                           + ~~想随时随地掏出来就写篇博文~~ < 先弄个好用的Markdown编辑器再说
                        
                        2. 为什么使用 [Termux](https://termux.com/)?
                        
                           + 手机没有root或者很难(无法)root
                           + 不想用Linux deploy
                        
                        3. 手机上部署的好处
                        
                           + 没有好处(bushi
                        
                           + > 不过手机上可以和电脑端进行多端同步，没NAS推荐OneDrive，其他云盘或者版本控制工具
                        
                        # 操作
                        
                        #### 一. 下载 Termux
                        
                        + 官网 [Termux](https://termux.com/) 
                        + [![](https://z3.ax1x.com/2021/08/12/fwf1Vf.png)](https://imgtu.com/i/fwf1Vf)
                        
                        #### 二. 命令部分
                        
                        1. 访问Android储存空间
                        
                           ```shell
                           termux-setup-storage
                           ```
                        
                           此时将会在 termux 根目录下创建一个 storage 文件夹，里面同步了 Android 内部储存的一些文件
                        
                        2. 下载 Nodejs
                        
                           ```shell
                           apt update
                           apt install nodejs
                           # 注：新版nodejs自带npm
                           apt install vim # 安装个vim编辑用
                           ```
                        
                           如果你觉得 `vim` 编辑太过抽象，可以使用外部编辑软件，编辑好了再上传到 Termux 内部空间 
                        
                           然后输入`node -v`,`npm -v` 查看是否安装成功
                        
                        3. 安装 Hexo
                        
                           ```shell
                           # 切换临时淘宝镜像
                           npm --registry https://registry.npm.taobao.org install express 
                           
                           npm install -g hexo-cli
                           ```
                        
                        4. 初始化 Hexo
                        
                           ```shell
                           hexo init my-blog # my-blog 可以换成你自己想用的文件夹名称
                           
                           cd my-blog
                           
                           npm install
                           ```
                        
                        5. 测试
                        
                           ```shell
                           hexo g # 生成网站文件
                           
                           hexo s # 启动 http 服务器
                           # 如果启动失败，可能是端口被占用了 输入 hexo s -p[指定端口]，访问时的端口也要改成对应端口
                           ```
                        
                           打开浏览器 ，输入 http://localhost:4000，查看是否启动成功
                        
                        #### 三. 部署
                        
                        #### 下面有两种方案，选择一个即可
                        
                        ---
                        
                        > 方案一：部署到 Github pages 上
                        >
                        > 优点：
                        >
                        > + 可以绑定一个无需备案的域名
                        > + 可以版本控制
                        > + 完全免费（自己的域名租赁费用除外）
                        >
                        > 缺点：
                        >
                        > + 搭建的静态博客访问较慢
                        > + 稳定性差
                        
                        1. 安装 Git
                        
                           ```bash
                           pkg install git
                           ```
                        
                           然后输入 `git --version` 查看是否安装成功
                        
                        2. 连接 GitHub
                        
                           如果你没有GitHub，那就注册一个
                        
                           输入
                        
                           ```bash
                           git config --global user.name "你的Github名称"
                           git config --global user.email "Github 邮箱"
                           # 可以不设置成 global，但谁在手机上还部署多个GitHub呢？
                           ```
                        
                           创建 SSH 密钥
                        
                           ```bash
                           ssh-keygen -t rsa -C "Github 邮箱"
                           # 输入Enter跳过要填的属性
                           ```
                        
                           不出意外的话，将会在 Termux 根目录下出现一个  `.ssh` 文件夹
                        
                           打开 `id_rsa.pub` ，复制里面的内容       注：不要将 `id_rsa` 文件内容泄露
                        
                           然后打开 Github，登录后点击头像，选择 Setting，找到 SSH and GPG keys，点击 New SSH key
                        
                           [![](https://z3.ax1x.com/2021/08/13/fDndoQ.png)](https://imgtu.com/i/fDndoQ)
                        
                           
                        
                           Key的内容粘贴复制的`id_rsa.pub`内容 ，Title随便填
                        
                           然后打开Ternmux，输入
                        
                           ```bash
                           ssh -T git@github.com
                           # 提示 Are you sure... 输入yes
                           # 显示 'Hi xxx!You've sucessfully...' 说明连接成功
                           ```
                        
                        3. 创建一个仓库
                        
                           仓库的名称输入 用户名.github.io
                        
                           勾选 'Initialize this repository with a README'
                        
                           仓库类型是 public 
                        
                           [![](https://z3.ax1x.com/2021/08/13/fDuF0S.png)](https://imgtu.com/i/fDuF0S)
                        
                           创建好了后就启动了一个HTTP服务器，可以通过 `https://用户名.github.io` 来访问
                        
                        4. 部署网页到 Github
                        
                           安装 hexo-deployer-git:
                        
                           ```bash
                           npm install hexo-deployer-git --save
                           ```
                        
                           打开 Hexo 根目录下单 _config.yml
                        
                           ```bash
                           vim _config.yml
                           ```
                        
                           修改 Hexo 根目录下单 _config.yml 
                        
                           ```yaml
                           deploy:
                            type: git
                            repository: git@github.com:[用户名]/[用户名].github.io.git
                            branch: master
                           ```
                        
                           改完保存
                        
                           ```
                           :wq
                           ```
                        
                           上传部署到 Github Pages
                        
                           ```bash
                           hexo d
                           ```
                        
                           OK！等待一会访问 `https://用户名.github.io` 就可以看到网站了
                        
                        5. 绑定自己的域名 （可选）
                        
                           找到自己域名的管理界面，新建解析
                        
                           记录类型选择 `CNAME` ，主机记录建议填 `blog`， 记录值填 `用户名.github.io`
                        
                           然后回到 Termux 的 Hexo 根目录
                        
                           ```bash
                           cd source
                           echo "[主机记录].[域名].[域名后缀]" > CNAME # 创建CNAME文件
                           ```
                        
                           部署
                        
                           ```bash
                           hexo cl # 清除
                           
                           hexo g -d # 生成页面并部署
                           ```
                        
                           开启 HTTPS（可选）
                        
                           打开博客所在的 Github 仓库，Setting->Github Pages
                        
                           `Custom domain` 里填 `CNAME` 里面的值
                        
                           勾选 `Enforce HTTPS`
                        
                           >  然后过一会就能使用自己的域名来访问博客了
                           >
                           >  接下来就可以diy自己的博客了
                           >
                           >  每次修改完都要输入 `hexo g -d` 来部署到远程服务器上
                        
                        ---
                        
                        > 方案二：部署到Tencent COS  (推荐)
                        >
                        > 优点：
                        >
                        > + 访问非常快
                        > + 访问稳定
                        > + 可以使用腾讯自带的CDN加速
                        >
                        > 缺点：
                        >
                        > + 要一点点钱 (相比与搭建在 Github Pages 上)，大概一天几分钱
                        > + 会被流量攻击导致欠费
                        > + 被绑定的域名需要备案
                        
                        >  手机端可能访问不了腾讯官网电脑版，建议使用一个可以模拟电脑端的浏览器，如 Chrome
                        
                        1. 准备
                        
                           + 去腾讯云申请 COS 服务 [传送门](https://cloud.tencent.com/product/cos?from=10680)
                           + 获取到 `APPID`，`SecretID`，`SecretKey`，`Bucket`，`Region` 这五个参数，前三个的位置在 控制台->右上角->访问管理->访问密钥
                           + 安装好`hexo-deployer-cos` Hexo 根目录输入命令 `npm install hexo-deployer-cos --save`
                           + 一颗聪明的脑袋
                        
                        2. 创建一个 `Bucket`
                        
                           + [腾讯云-COS 控制台](https://console.cloud.tencent.com/cos5)
                        
                           [![](https://z3.ax1x.com/2021/08/10/fJrGLt.png)](https://imgtu.com/i/fJrGLt)
                        
                           [![](https://z3.ax1x.com/2021/08/10/fJrryn.png)](https://imgtu.com/i/fJrryn)
                        
                           然后就是本地 Hexo 的配置文件
                        
                           打开 `_config.yml`
                        
                           ```bash
                           vim _config.yml
                           ```
                        
                           编辑 `_config.yml`
                        
                           ```yaml
                           deploy: 
                             type: cos
                             appId: [APPID]
                             secretId: [SecretID]
                             secretKey: [SecretKey]
                             bucket: [BucketName-APPID]
                             region: [Region]
                             # 以上用'[]'的均填图片内的对应内容或者访问的密钥内容
                           ```
                        
                           保存退出 
                        
                           ```
                           :wq
                           ```
                        
                           部署
                        
                           ```bash
                           hexo cl
                           
                           hexo g -d
                           ```
                        
                           打开 Bucket 查看储存，是否已经将本地内容提交
                        
                           这样就行了，访问图片里的`静态网站访问域名`即可查看部署的网站
                        
                        3. 绑定自己的域名的话看腾讯的官方文档吧，我这里没有备案的域名就无法演示了
                        
                        ---
                        
                        #### 四. 向 Termux 内部储存添加文件
                        
                        1. 使用 `wget`
                        
                           将文件上传到网上，然后复制 url，在 Termux 里使用 `wget url` 即可获取
                        
                        2. 使用内部储存
                        
                           在上面使用了 `termux-setup-storage` 后， 根目录出现了 `storage` 文件夹
                        
                           里面有 `Downloads`，`dcim`，`movies` 等，分别同步了手机内部储存的对应文件夹
                        
                           以 `Downloads` 为例，将所需要的文件移动到该文件夹下，在 Termux 内便能访问
                        
                        3. 使用 Termux 打开文件
                        
                           手机上选择一个文件的打开方式的时候，可以选择使用 Termux 打开，之后便会出现在 Termux 根目录下的 `downloads` 文件夹内 (注：不是系统内的 `Downloads` 文件夹)
                        
                        # 主题&优化
                        
                        #### 一. 主题
                        
                        1. 推荐(其他的并不了解就不贴出了)
                           + 花哨一点: [Matery](https://github.com/blinkfox/hexo-theme-matery)
                           + 十分简洁: [Aircloud](https://github.com/aircloud/hexo-theme-aircloud)
                        2. 本站主题目前采用的是 Matery，走的是简单的风格
                        3. 如果部署到 Github Page 的话，建议使用简单一点的主题，这样资源读取会快一些
                        
                        #### 二. 优化
                        
                        1. 主题优化 (按照本站的优化)
                        
                           + 关闭了每日切换 banner 图
                           + 删了一些不必要的控件(banner 第二个按键和右上角的Github猫)
                           + 安装了中文 url 转拼音的插件，以便 `SEO`
                           + 关闭了留言板
                           + 新增一个 about/other 页面详细介绍
                           + 开启了音乐
                           + ...
                        
                        2. 访问优化
                        
                           + 选择性开启 jsDeliver 并部署到相应平台 (Matery 主题可以用这个功能)
                        
                             > 选择性: 由于本站是部署在腾讯云 `COS` 和  `Github Pages` 上的，对于 `Github Pages` 上的页面采用了 jsDeliever 来进行全站 CDN 加速，而对于 腾讯云 `COS`，采用 jsDeliver 会使得访问速度下降，于是便关闭了此功能部署到 `COS` 上
                             >
                             > 我写了一个脚本，可以自动选择性部署，如果你也有这种需求，可以联系我或者评论
                             >
                             > + [![](https://z3.ax1x.com/2021/08/12/fwo83D.md.png)](https://imgtu.com/i/fwo83D)
                             >
                             > 另外，你也可以在 jsDeliever 那栏填入 腾讯云的博客对象储存桶地址，然后部署到 `Github Pages` 上这样采用的就是 `腾讯云对象储存加速?`，访问 Github Pages 时会回调到 腾讯云对象储存桶里拿取文件，但会产生流量
                        
                           + 使用 Gulp 进行代码压缩 (转载来自 [Hexo进阶之各种优化 | Sky03's Blog](https://blog.sky03.cn/posts/42790.html#toc-heading-11))
                        
                             > 虽然几个空格空行占不了多少字节，但是访问量一大，那些空格空行就会有一点程度影响，能省一点是一点咯
                             >
                             > 1. 在 Hexo 根目录下打开 shell
                             >
                             >    ```bash
                             >    # 全局安装gulp模块
                             >    npm install gulp -g
                             >    # 安装各种小功能模块  执行这步的时候，可能会提示权限的问题，最好以管理员模式执行
                             >    npm install gulp gulp-htmlclean gulp-htmlmin gulp-minify-css gulp-uglify gulp-imagemin --save
                             >    # 额外的功能模块
                             >    npm install gulp-debug gulp-clean-css gulp-changed gulp-if gulp-plumber gulp-babel babel-preset-es2015 del @babel/core --save
                             >    ```
                             >
                             > 2. 在Hexo根目录新建文件 `gulpfile.js`，并复制以下内容到文件中，有中文注释，可以根据自己需求修改。
                             >
                             >    ```bash
                             >    # Termux bash
                             >    touch gulpfile.js # 新建文件
                             >    
                             >    vim gulpfile.js
                             >    ```
                             >
                             >    复制以下内容
                             >
                             >    ```js
                             >    var gulp = require("gulp");
                             >    var debug = require("gulp-debug");
                             >    var cleancss = require("gulp-clean-css"); //css压缩组件
                             >    var uglify = require("gulp-uglify"); //js压缩组件
                             >    var htmlmin = require("gulp-htmlmin"); //html压缩组件
                             >    var htmlclean = require("gulp-htmlclean"); //html清理组件
                             >    var imagemin = require("gulp-imagemin"); //图片压缩组件
                             >    var changed = require("gulp-changed"); //文件更改校验组件
                             >    var gulpif = require("gulp-if"); //任务 帮助调用组件
                             >    var plumber = require("gulp-plumber"); //容错组件（发生错误不跳出任务，并报出错误内容）
                             >    var isScriptAll = true; //是否处理所有文件，(true|处理所有文件)(false|只处理有更改的文件)
                             >    var isDebug = true; //是否调试显示 编译通过的文件
                             >    var gulpBabel = require("gulp-babel");
                             >    var es2015Preset = require("babel-preset-es2015");
                             >    var del = require("del");
                             >    var Hexo = require("hexo");
                             >    var hexo = new Hexo(process.cwd(), {}); // 初始化一个hexo对象
                             >    
                             >    // 清除public文件夹
                             >    gulp.task("clean", function () {
                             >        return del(["public/**/*"]);
                             >    });
                             >    
                             >    // 下面几个跟hexo有关的操作，主要通过hexo.call()去执行，注意return
                             >    // 创建静态页面 （等同 hexo generate）
                             >    gulp.task("generate", function () {
                             >        return hexo.init().then(function () {
                             >            return hexo
                             >                .call("generate", {
                             >                    watch: false
                             >                })
                             >                .then(function () {
                             >                    return hexo.exit();
                             >                })
                             >                .catch(function (err) {
                             >                    return hexo.exit(err);
                             >                });
                             >        });
                             >    });
                             >    
                             >    // 启动Hexo服务器
                             >    gulp.task("server", function () {
                             >        return hexo
                             >            .init()
                             >            .then(function () {
                             >                return hexo.call("server", {});
                             >            })
                             >            .catch(function (err) {
                             >                console.log(err);
                             >            });
                             >    });
                             >    
                             >    // 部署到服务器
                             >    gulp.task("deploy", function () {
                             >        return hexo.init().then(function () {
                             >            return hexo
                             >                .call("deploy", {
                             >                    watch: false
                             >                })
                             >                .then(function () {
                             >                    return hexo.exit();
                             >                })
                             >                .catch(function (err) {
                             >                    return hexo.exit(err);
                             >                });
                             >        });
                             >    });
                             >    
                             >    // 压缩public目录下的js文件
                             >    gulp.task("compressJs", function () {
                             >        return gulp
                             >            .src(["./public/**/*.js", "!./public/libs/**"]) //排除的js
                             >            .pipe(gulpif(!isScriptAll, changed("./public")))
                             >            .pipe(gulpif(isDebug, debug({ title: "Compress JS:" })))
                             >            .pipe(plumber())
                             >            .pipe(
                             >                gulpBabel({
                             >                    presets: [es2015Preset] // es5检查机制
                             >                })
                             >            )
                             >            .pipe(uglify()) //调用压缩组件方法uglify(),对合并的文件进行压缩
                             >            .pipe(gulp.dest("./public")); //输出到目标目录
                             >    });
                             >    
                             >    // 压缩public目录下的css文件
                             >    gulp.task("compressCss", function () {
                             >        var option = {
                             >            rebase: false,
                             >            //advanced: true, //类型：Boolean 默认：true [是否开启高级优化（合并选择器等）]
                             >            compatibility: "ie7" //保留ie7及以下兼容写法 类型：String 默认：''or'*' [启用兼容模式； 'ie7'：IE7兼容模式，'ie8'：IE8兼容模式，'*'：IE9+兼容模式]
                             >            //keepBreaks: true, //类型：Boolean 默认：false [是否保留换行]
                             >            //keepSpecialComments: '*' //保留所有特殊前缀 当你用autoprefixer生成的浏览器前缀，如果不加这个参数，有可能将会删除你的部分前缀
                             >        };
                             >        return gulp
                             >            .src(["./public/**/*.css", "!./public/**/*.min.css"]) //排除的css
                             >            .pipe(gulpif(!isScriptAll, changed("./public")))
                             >            .pipe(gulpif(isDebug, debug({ title: "Compress CSS:" })))
                             >            .pipe(plumber())
                             >            .pipe(cleancss(option))
                             >            .pipe(gulp.dest("./public"));
                             >    });
                             >    
                             >    // 压缩public目录下的html文件
                             >    gulp.task("compressHtml", function () {
                             >        var cleanOptions = {
                             >            protect: /<\!--%fooTemplate\b.*?%-->/g, //忽略处理
                             >            unprotect: /<script [^>]*\btype="text\/x-handlebars-template"[\s\S]+?<\/script>/gi //特殊处理
                             >        };
                             >        var minOption = {
                             >            collapseWhitespace: true, //压缩HTML
                             >            collapseBooleanAttributes: true, //省略布尔属性的值 <input checked="true"/> ==> <input />
                             >            removeEmptyAttributes: true, //删除所有空格作属性值 <input id="" /> ==> <input />
                             >            removeScriptTypeAttributes: true, //删除<script>的type="text/javascript"
                             >            removeStyleLinkTypeAttributes: true, //删除<style>和<link>的type="text/css"
                             >            removeComments: true, //清除HTML注释
                             >            minifyJS: true, //压缩页面JS
                             >            minifyCSS: true, //压缩页面CSS
                             >            minifyURLs: true //替换页面URL
                             >        };
                             >        return gulp
                             >            .src("./public/**/*.html")
                             >            .pipe(gulpif(isDebug, debug({ title: "Compress HTML:" })))
                             >            .pipe(plumber())
                             >            .pipe(htmlclean(cleanOptions))
                             >            .pipe(htmlmin(minOption))
                             >            .pipe(gulp.dest("./public"));
                             >    });
                             >    
                             >    // 压缩 public/medias 目录内图片
                             >    gulp.task("compressImage", function () {
                             >        var option = {
                             >            optimizationLevel: 5, //类型：Number 默认：3 取值范围：0-7（优化等级）
                             >            progressive: true, //类型：Boolean 默认：false 无损压缩jpg图片
                             >            interlaced: false, //类型：Boolean 默认：false 隔行扫描gif进行渲染
                             >            multipass: false //类型：Boolean 默认：false 多次优化svg直到完全优化
                             >        };
                             >        return gulp
                             >            .src("./public/medias/**/*.*")
                             >            .pipe(gulpif(!isScriptAll, changed("./public/medias")))
                             >            .pipe(gulpif(isDebug, debug({ title: "Compress Images:" })))
                             >            .pipe(plumber())
                             >            .pipe(imagemin(option))
                             >            .pipe(gulp.dest("./public"));
                             >    });
                             >    // 执行顺序： 清除public目录 -> 产生原始博客内容 -> 执行压缩混淆 -> 部署到服务器
                             >    gulp.task(
                             >        "build",
                             >        gulp.series(
                             >            "clean",
                             >            "generate",
                             >            "compressHtml",
                             >            "compressCss",
                             >            "compressJs",
                             >            "compressImage",
                             >            gulp.parallel("deploy")
                             >        )
                             >    );
                             >    
                             >    // 默认任务
                             >    gulp.task(
                             >        "default",
                             >        gulp.series(
                             >            "clean",
                             >            "generate",
                             >            gulp.parallel("compressHtml", "compressCss", "compressJs","compressImage")
                             >        )
                             >    );
                             >    //Gulp4最大的一个改变就是gulp.task函数现在只支持两个参数，分别是任务名和运行任务的函数
                             >    ```
                             >
                             > 3. 以后的执行方式有两种：
                             >
                             >    + 直接在Hexo根目录执行 `gulp`或者 `gulp default` ，这个命令相当于 `hexo cl&&hexo g` 并且再把代码和图片压缩。
                             >    + 在Hexo根目录执行 `gulp build` ，这个命令与第1种相比是：在最后又加了个 `hexo d` ，等于说生成、压缩文件后又帮你自动部署了。
                             >
                             > 4. 值得注意的是：这个加入了图片压缩，如果不想用图片压缩可以把第154行的 `"compressImage",` 和第165行的 `,"compressImage"` 去掉即可
                        
                        # 结语
                        
                        + 主要还是介绍一下 `Termux` 这款终端模拟器，以及 Hexo 博客的搭建和部署的两种方式
                        + 如果你跟着教程搭建好了博客，记得评论一下啊 : )
                    """.trimIndent()
                )
            }
        }
    }

}