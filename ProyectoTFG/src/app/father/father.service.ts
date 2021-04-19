import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class FatherService {
  url: string = '/cf4j'
  constructor(private http: HttpClient) { }


  loadMFC(body) {
    return this.http.post(this.url + '/matrixFactorizationComparison',body);
  }
}