import { Component, OnInit } from '@angular/core';
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { FatherService } from './father.service';
import { FormGroup, FormControl, Validators } from '@angular/forms';
// import { Chart } from 'chart.js';
@Component({
  selector: 'app-father',
  templateUrl: './father.component.html',
  styleUrls: ['./father.component.css']
})
export class FatherComponent {

  alg = [
    {
      name: 'PMF',
      color: 'rgb(255, 99, 132)'
    },
    {
      name: 'BNMF',
      color: 'rgb(153, 102, 255)'
    },
    {
      name: 'BiasedMF',
      color: 'rgb(177, 79, 164)'
    },
    {
      name: 'NMF',
      color: 'rgb(107, 9, 91)'
    },
    {
      name: 'CLiMF',
      color: 'rgb(69, 107, 9)'
    },
    {
      name: 'SVDPlusPlus',
      color: 'rgb(171, 218, 64)'
    },
    {
      name: 'HPF',
      color: 'rgb(75, 192, 192)'
    },
    {
      name: 'URP',
      color: 'rgb(228, 114, 8)'
    }
  ];

  algDefault = [
    {
      name: 'PMF',
      color: 'rgb(255, 99, 132)'
    },
    {
      name: 'BNMF',
      color: 'rgb(153, 102, 255)'
    },
    {
      name: 'BiasedMF',
      color: 'rgb(177, 79, 164)'
    },
    {
      name: 'NMF',
      color: 'rgb(107, 9, 91)'
    },
    {
      name: 'CLiMF',
      color: 'rgb(69, 107, 9)'
    },
    {
      name: 'SVDPlusPlus',
      color: 'rgb(171, 218, 64)'
    },
    {
      name: 'HPF',
      color: 'rgb(75, 192, 192)'
    },
    {
      name: 'URP',
      color: 'rgb(228, 114, 8)'
    }
  ];

  done = [
  ];

  datasets = [{
    name: 'TMovieLens100K',
    color: 'rgb(255, 255, 255)'
  },
  {
    name: 'MovieLens1M',
    color: 'rgb(255, 255, 255)'
  },
  {
    name: 'MovieLens10M',
    color: 'rgb(255, 255, 255)'
  },
  {
    name: 'FilmTrust',
    color: 'rgb(255, 255, 255)'
  }, {
    name: 'BookCrossing',
    color: 'rgb(255, 255, 255)'
  }, {
    name: 'LibimSeTi',
    color: 'rgb(255, 255, 255)'
  }, {
    name: 'MyAnimeList',
    color: 'rgb(255, 255, 255)'
  }, {
    name: 'Jester',
    color: 'rgb(255, 255, 255)'
  }, {
    name: 'Netflix Prize',
    color: 'rgb(255, 255, 255)'
  }]

  datasetsDefault = [{
    name: 'TMovieLens100K',
    color: 'rgb(255, 255, 255)'
  },
  {
    name: 'MovieLens1M',
    color: 'rgb(255, 255, 255)'
  },
  {
    name: 'MovieLens10M',
    color: 'rgb(255, 255, 255)'
  },
  {
    name: 'FilmTrust',
    color: 'rgb(255, 255, 255)'
  }, {
    name: 'BookCrossing',
    color: 'rgb(255, 255, 255)'
  }, {
    name: 'LibimSeTi',
    color: 'rgb(255, 255, 255)'
  }, {
    name: 'MyAnimeList',
    color: 'rgb(255, 255, 255)'
  }, {
    name: 'Jester',
    color: 'rgb(255, 255, 255)'
  }, {
    name: 'Netflix Prize',
    color: 'rgb(255, 255, 255)'
  }]



  ranges = [5, 10, 15, 20, 25, 30, 35, 40, 45, 50];
  selectRanges = [];
  submit = false;
  fromRun: FormGroup;
  fromAdd: FormGroup;

  resultParam = [];
  resultOK: boolean = false;
  spinner: boolean = false;



  constructor(private service: FatherService) {

    this.fromRun = new FormGroup({
      range: new FormControl({ value: null, disabled: false }, [Validators.required])
    });
    this.fromAdd = new FormGroup({
      range: new FormControl({ value: null, disabled: false }, [Validators.required])
    });


  }

  ngOnInit() {

  }

  private ngOnDestroy(): void {
  }

  drop(event: CdkDragDrop<string[]>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex);
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
      let board = [];
      vm.spinner = true;
      Object.assign(board, vm.done);
      let ds = board.shift()
      let body = {
        dataset: ds.name,
        param: vm.selectRanges,
        algorithms: vm.getAlg(board)
      };
      vm.service.loadMFC(body).subscribe({
        next(data) {
          vm.spinner = false;
          let result: any = data;
          vm.resultParam = [];
          result.forEach((element, index) => {
            vm.resultParam.push(
              {
                label: element.algorithm,
                data: element.results,
                fill: false,
                borderColor: vm.algDefault.find(alg => alg.name === element.algorithm).color,
                tension: 0.1
              }
            );
            // vm.resultParam = element.results;
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

}
