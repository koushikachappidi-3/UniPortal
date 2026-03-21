document.addEventListener("DOMContentLoaded", function () {
    // Toast auto-dismiss + close
    document.querySelectorAll(".js-toast").forEach(function (toast) {
        var closeBtn = toast.querySelector(".toast-close");
        if (closeBtn) {
            closeBtn.addEventListener("click", function () {
                toast.remove();
            });
        }
        setTimeout(function () {
            toast.classList.add("toast-hide");
            setTimeout(function () {
                toast.remove();
            }, 250);
        }, 4200);
    });

    // Styled modal confirmations for destructive actions
    var modal = document.getElementById("confirmModal");
    if (modal) {
        var messageEl = document.getElementById("confirmModalMessage");
        var confirmBtn = document.getElementById("confirmModalConfirm");
        var cancelBtn = document.getElementById("confirmModalCancel");
        var targetForm = null;

        document.querySelectorAll(".js-confirm-form").forEach(function (form) {
            form.addEventListener("submit", function (event) {
                event.preventDefault();
                targetForm = form;
                messageEl.textContent = form.getAttribute("data-confirm-message") || "Are you sure?";
                modal.classList.add("show");
            });
        });

        confirmBtn.addEventListener("click", function () {
            if (targetForm) {
                targetForm.submit();
            }
        });

        function closeModal() {
            modal.classList.remove("show");
            targetForm = null;
        }

        cancelBtn.addEventListener("click", closeModal);
        modal.addEventListener("click", function (event) {
            if (event.target === modal) {
                closeModal();
            }
        });
    }
});


