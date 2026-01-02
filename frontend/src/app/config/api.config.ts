export const API_CONFIG = {
  baseUrl: 'http://localhost:8080/api',
  endpoints: {
    clients: '/clients/',
    invoices: '/invoices/',
  }
};

export const getClientApiUrl = () => `${API_CONFIG.baseUrl}${API_CONFIG.endpoints.clients}`;
export const getInvoiceApiUrl = () => `${API_CONFIG.baseUrl}${API_CONFIG.endpoints.invoices}`;

