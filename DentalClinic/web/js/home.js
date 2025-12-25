// Home Page JavaScript
function loadPage(page) {
    const servicesContainer = document.querySelector('.services');
    const paginationContainer = document.querySelector('.pagination');
    
    servicesContainer.style.opacity = '0.5';
    servicesContainer.style.transition = 'opacity 0.3s ease';
    
    fetch('home?page=' + page)
        .then(response => response.text())
        .then(html => {
            const parser = new DOMParser();
            const doc = parser.parseFromString(html, 'text/html');
            
            const newServices = doc.querySelector('.services');
            const newPagination = doc.querySelector('.pagination');
            
            if (newServices) {
                servicesContainer.innerHTML = newServices.innerHTML;
                servicesContainer.style.opacity = '1';
            }
            
            if (newPagination) {
                paginationContainer.innerHTML = newPagination.innerHTML;
            }
        })
        .catch(error => {
            console.error('Error:', error);
            servicesContainer.innerHTML = '<div style="text-align: center; color: red;">Error loading page</div>';
            servicesContainer.style.opacity = '1';
        });
}




