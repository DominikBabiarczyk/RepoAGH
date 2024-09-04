import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PostData } from "./PostData";

@Injectable({
  providedIn: 'root'
})
export class HttpService {
  url: string = "http://localhost:8080/test";

  constructor(private http: HttpClient) { }

  calculate(postData: PostData): Observable<any> {
    return this.http.post(this.url, postData);
  }
}
