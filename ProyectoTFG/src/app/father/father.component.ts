import { Component, OnInit } from '@angular/core';
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { FatherService } from './father.service';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Observable } from 'rxjs';
import { Dataset } from '../models/dataset.model';
import { QualityMesure } from '../models/qualityMesure.model';
import { Graphic } from '../models/graphic.model';

@Component({
  selector: 'app-father',
  templateUrl: './father.component.html',
  styleUrls: ['./father.component.css']
})
export class FatherComponent {

  //Recogidas by Json
  qualityMeasure: Observable<QualityMesure[]>
  alg: Observable<Algorithm[]>
  datasets: Observable<Dataset[]>

  //Tablero
  done = [];

  //Restablecen los valores por defecto
  algDefault: Algorithm[] = [];
  datasetsDefault: Dataset[] = []

  //Parametros disponibles en los algoritmos
  paramsAvailable: any = []

  //Valores dinamicos de la
  selectRanges: number[] = [];

  //Accion de consulta
  submit: boolean = false;

  //From del formulario del tablero
  fromAdd: FormGroup;

  //Datos a pintar en la grafica
  resultParam: Graphic[] = [];

  //Resultado del back
  resultOK: boolean = false;

  //Control Spinner
  spinner: boolean = false;



  constructor(private service: FatherService) {
    this.fromAdd = new FormGroup({
      range: new FormControl({ value: null, disabled: false }, [Validators.required]),
      valuesDynamic: new FormControl({ value: null, disabled: false }, [Validators.required]),
      semilla: new FormControl({ value: null, disabled: false }),
      qualityMeasure: new FormControl({ value: null, disabled: false }),
      recommendations: new FormControl({ value: null, disabled: false }),
      threshold: new FormControl({ value: null, disabled: false })
    });
  }

  ngOnInit() {
    this.alg = this.service.getAlgorihms();
    this.datasets = this.service.getDatasets();
    this.qualityMeasure = this.service.getQualityMesure();

    Object.assign(this.datasetsDefault, this.datasets);
    Object.assign(this.algDefault, this.alg);
  }


  drop(event: CdkDragDrop<string[]>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex);
      this.getParamsAvailable();
    }
  }

  resetBoard() {
    this.done = [];
    Object.assign(this.datasets, this.datasetsDefault);
    Object.assign(this.alg, this.algDefault);
    this.submit = false;
    this.resultOK = false;

  }

  applyBoard() {

    if (this.camposCompleted()) {
      let vm = this;
      let board = JSON.parse(JSON.stringify(vm.done));
      vm.spinner = true;
      let ds = board.shift();
      if (this.fromAdd.value.semilla) {
        board.forEach(element => {
          if (element.params) {
            element.params.push({
              label: 'Semilla',
              key: 'seed',
              value: ''
            })
          }
        });
      }
      let body = {
        dataset: ds.name,
        rangeDynamic: {
          name: board[0].rangeDynamic.key,
          range: vm.selectRanges
        },
        qualityMeasure: {
          name: this.fromAdd.value.qualityMeasure.name,
          numberOfRecommendations: this.fromAdd.value.qualityMeasure.numberOfRecommendations ? this.fromAdd.value.recommendations : null,
          relevantThreshold: this.fromAdd.value.qualityMeasure.relevantThreshold ? this.fromAdd.value.threshold : null,
        },
        algorithms: board
      };
      vm.service.loadMFC(body).subscribe({
        next(data) {
          vm.spinner = false;
          let result: any = data;
          vm.resultParam = [];
          result.forEach((element, index) => {
            vm.resultParam.push(
              {
                label: element.algorithm.name,
                data: element.results,
                fill: false,
                borderColor: body.algorithms[index].color,
                tension: 0.1
              }
            );
          });

          vm.resultOK = true;
        },
        error(msg) {
          vm.spinner = false;
          alert('Error el la comunicacíon con el servidor')
        }
      });
    }
  }

  getAlg(board) {
    let result = [];
    board.forEach(element => {
      result.push(element.name)
    });
    return result;
  }

  camposCompleted() {
    return this.done.length && this.selectRanges.length
  }

  isAlg(obj) {
    return this.algDefault.find(alg => alg.name === obj)
  }

  changeColor(item, color) {
    let intro = document.getElementById(item);
    intro.style.background = color;
  }

  getParamsAvailable() {
    if (this.done.length > 1) {
      if (this.done.length === 2) {
        this.paramsAvailable = this.paramsAvailable.concat(this.done[1].params);
      } else {
        for (let index = 2; index < this.done.length; index++) {
          this.paramsAvailable = this.paramsAvailable.filter((valor, indice) => {
            return this.done[index].params.findIndex((val) => val.key === valor.key) != -1;
          })
        }
      }
    }
  }

  changeType() {
    if (this.fromAdd.value.valuesDynamic) {
      this.selectRanges = [];
      this.fromAdd.patchValue({
        range: null
      })
    }
    this.resultOK = false;
    this.submit = false;
  }

  pushRange() {
    if (this.selectRanges.length < 14 && this.fromAdd.value.range != null) {
      this.selectRanges.push(this.fromAdd.value.range)
    }
  }

}