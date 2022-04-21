import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PageNotFoundComponent } from './components/common/page-not-found/page-not-found.component';
import { IndexMatchComponent } from './components/match/index-match/index-match.component';
import { ViewMatchComponent } from './components/match/view-match/view-match.component';

const routes: Routes = [
  { path: '', component: IndexMatchComponent },
  { path: 'match/index', component: IndexMatchComponent },
  { path: 'match/view/:matchID', component: ViewMatchComponent },
  { path: 'page-not-found', component: PageNotFoundComponent },
  { path: '**', redirectTo: 'page-not-found', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
