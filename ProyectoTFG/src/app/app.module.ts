import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { DragDropModule } from '@angular/cdk/drag-drop';

import { AppComponent } from './app.component';
import { FatherComponent } from './father/father.component';

import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ChartsModule } from 'ng2-charts';
import { Chart, registerables } from 'chart.js';
import { GraphicComponent } from './graphic/graphic.component';

@NgModule({
  declarations: [
    AppComponent,
    FatherComponent,
    GraphicComponent
  ],
  imports: [
    BrowserModule,
    DragDropModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    ChartsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
