import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { IndexMatchComponent } from './components/match/index-match/index-match.component';
import { ViewMatchComponent } from './components/match/view-match/view-match.component';

import { LoadingComponent } from './components/common/loading/loading.component';
import { PageNotFoundComponent } from './components/common/page-not-found/page-not-found.component';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgxChartsModule } from '@swimlane/ngx-charts';

import { MatButtonModule } from '@angular/material/button';
import { MatDividerModule } from '@angular/material/divider';
import { EXPANSION_PANEL_ANIMATION_TIMING, MatExpansionModule } from '@angular/material/expansion';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatTableModule } from '@angular/material/table';
import { MatToolbarModule } from '@angular/material/toolbar';
import { OddsTableComponent } from './components/match/odds-table/odds-table.component';

@NgModule({
  declarations: [
    AppComponent,
    IndexMatchComponent,
    ViewMatchComponent,
    LoadingComponent,
    PageNotFoundComponent,
    OddsTableComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    BrowserAnimationsModule,

    NgbModule,
    NgxChartsModule,

    MatButtonModule,
    MatDividerModule,
    MatExpansionModule,
    MatIconModule,
    MatListModule,
    MatProgressSpinnerModule,
    MatSlideToggleModule,
    MatTableModule,
    MatToolbarModule
  ],
  providers: [
    {
      provide: EXPANSION_PANEL_ANIMATION_TIMING,
      useValue: "0ms"
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
