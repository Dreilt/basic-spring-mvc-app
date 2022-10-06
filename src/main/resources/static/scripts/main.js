function setCurrentSortOption() {
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const sortBy = urlParams.get('sort_by');
    let orderBy = urlParams.get('order_by');
    let activeLink;

    if (sortBy == null && orderBy == null) {
        activeLink = document.querySelector('#lastNameAsc');
    }

    if (sortBy != null && !orderBy != null) {
        orderBy = orderBy.charAt(0).toUpperCase() + orderBy.slice(1);
        activeLink = document.querySelector(`#${sortBy}${orderBy}`);
    }

    document.getElementById('sortOptionsMenu').textContent=activeLink.textContent;
    activeLink.className='dropdown-item bold-text';
}