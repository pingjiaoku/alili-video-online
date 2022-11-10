let draging = null
channel.ondragstart = function(event) {
    console.log("start:")
    // dataTransfer.setData把拖动对象的数据存入其中，可以用dataTransfer.getData来获取数据
    event.dataTransfer.setData("te", event.target.innerText)
    draging = event.target
}
channel.ondragover = function(event) {
    console.log("over:")
    // 默认地，无法将数据/元素放置到其他元素中。如果需要设置允许放置，必须阻止对元素的默认处理方式
    event.preventDefault()
    let target = event.target
    if (target.nodeName === "LI" && target !== draging) {
        // 获取初始位置
        let targetRect = target.getBoundingClientRect()
        let dragingRect = draging.getBoundingClientRect()
        if (target) {
            // 判断是否动画元素
            if (target.animated) {
                return;
            }
        }
        if (_index(draging) < _index(target)) {
            // 目标比元素大，插到其后面
            // extSibling下一个兄弟元素
            target.parentNode.insertBefore(draging, target.nextSibling)
        } else {
            // 目标比元素小，插到其前面
            target.parentNode.insertBefore(draging, target)
        }
        _animate(dragingRect, draging)
        _animate(targetRect, target)
    }
}
// 获取元素在父元素中的index
function _index(el) {
    let index = 0
    if (!el || !el.parentNode) {
        return -1
    }
    // previousElementSibling：上一个兄弟元素
    while (el && (el = el.previousElementSibling)) {
        index++
    }
    return index
}
// 触发动画
function _animate(prevRect, target) {
    let ms = 300
    if (ms) {
        let currentRect = target.getBoundingClientRect()
        if (prevRect.nodeType === 1) {
            prevRect = prevRect.getBoundingClientRect()
        }
        _css(target, 'transition', 'none')
        _css(target, 'transform', 'translate3d(' +
            (prevRect.left - currentRect.left) + 'px,' +
            (prevRect.top - currentRect.top) + 'px,0)'
        );

        target.offsetWidth; // 触发重绘

        _css(target, 'transition', 'all ' + ms + 'ms');
        _css(target, 'transform', 'translate3d(0,0,0)');
        // 事件到了之后把transition和transform清空
        clearTimeout(target.animated);
        target.animated = setTimeout(function() {
            _css(target, 'transition', '');
            _css(target, 'transform', '');
            target.animated = false;
        }, ms);
    }
}
// 给元素添加style
function _css(el, prop, val) {
    let style = el && el.style
    if (style) {
        if (val === void 0) {
            if (document.defaultView && document.defaultView.getComputedStyle) {
                val = document.defaultView.getComputedStyle(el, '')
            } else if (el.currentStyle) {
                val = el.currentStyle
            }
            return prop === void 0 ? val : val[prop]
        } else {
            if (!(prop in style)) {
                prop = '-webkit-' + prop;
            }
            style[prop] = val + (typeof val === 'string' ? '' : 'px')
        }
    }
}