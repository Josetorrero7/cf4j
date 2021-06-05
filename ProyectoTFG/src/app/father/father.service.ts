import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import * as algorithms from '../../assets/algorithms.json';
import * as datasets from '../../assets/datasets.json';
import * as qualityMesure from '../../assets/qualityMesure.json';
import { Algorithm } from '../models/algorithm.model';
import { Dataset } from '../models/dataset.model';
import { QualityMesure } from '../models/qualityMesure.model';

@Injectable({
  providedIn: 'root',
})
export class FatherService {
  url: string = '/cf4j'
  constructor(private http: HttpClient) { }


  loadMFC(body) {
    return this.http.post(this.url + '/matrixFactorizationComparison',body);
  }

  getAlgorihms(): Observable<Array<Algorithm>> {
    return (algorithms as any).default;
  }
  getDatasets(): Observable<Array<Dataset>> {
    return (datasets as any).default;
  }
  getQualityMesure(): Observable<Array<QualityMesure>> {
    return (qualityMesure as any).default;
  }
}