(function ($, $w) {
    $(document).ready(function () {
        console.log("width: " + innerWidth + " height: " + innerHeight);
        setPage();
    });

    /**
     * 设置页面尺寸
     */
    function setPage() {
        $(".content").css({
            "width": innerWidth + "px",
            "height": innerHeight - headerHeight + "px"
        });
    }

    var innerWidth = $w.innerWidth;
    var innerHeight = $w.innerHeight;
    var headerHeight = 80;
})(jQuery, window);