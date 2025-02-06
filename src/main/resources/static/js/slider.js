(function () {
    const $right_arrow = document.getElementById("right-arrow");
    const $left_arrow = document.getElementById("left-arrow");

    const animate = (direction) => {
        document.querySelectorAll(".slide").forEach((slide) => {
            const $bg_prev = slide.querySelector(".bg-previous");
            const $bg_curr = slide.querySelector(".bg-current");
            const $bg_next = slide.querySelector(".bg-next");
            const $bg_next_next = slide.querySelector(".bg-next-next");

            if (direction === "next") {
                // Rotate the images forward
                const nextBg = $bg_prev.getAttribute("data-bg");
                $bg_prev.setAttribute("data-bg", $bg_curr.getAttribute("data-bg"));
                $bg_curr.setAttribute("data-bg", $bg_next.getAttribute("data-bg"));
                $bg_next.setAttribute("data-bg", $bg_next_next.getAttribute("data-bg"));
                $bg_next_next.setAttribute("data-bg", nextBg);
            } else {
                // Rotate the images backward
                const prevBg = $bg_next_next.getAttribute("data-bg");
                $bg_next_next.setAttribute("data-bg", $bg_next.getAttribute("data-bg"));
                $bg_next.setAttribute("data-bg", $bg_curr.getAttribute("data-bg"));
                $bg_curr.setAttribute("data-bg", $bg_prev.getAttribute("data-bg"));
                $bg_prev.setAttribute("data-bg", prevBg);
            }

            // Update the background-image styles dynamically
            $bg_prev.style.backgroundImage = `url(${$bg_prev.getAttribute("data-bg")})`;
            $bg_curr.style.backgroundImage = `url(${$bg_curr.getAttribute("data-bg")})`;
            $bg_next.style.backgroundImage = `url(${$bg_next.getAttribute("data-bg")})`;
            $bg_next_next.style.backgroundImage = `url(${$bg_next_next.getAttribute("data-bg")})`;
        });
    };

    $right_arrow.addEventListener("click", (e) => {
        e.preventDefault();
        animate("next");
    });

    $left_arrow.addEventListener("click", (e) => {
        e.preventDefault();
        animate("previous");
    });

    // Initialize the images on page load
    document.querySelectorAll(".slide").forEach((slide) => {
        slide.querySelectorAll(".slide-bg").forEach((bg) => {
            bg.style.backgroundImage = `url(${bg.getAttribute("data-bg")})`;
        });
    });
})();