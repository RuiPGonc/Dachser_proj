/** Shape of the error body returned by the backend on 4xx/5xx responses. */
export interface ErrorResponse {
  status: number;
  message: string;
  timestamp: string;
}
