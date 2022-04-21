import { HttpClient } from '@angular/common/http';
import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { MatAccordion } from '@angular/material/expansion';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-view-match',
  templateUrl: './view-match.component.html',
  styleUrls: ['./view-match.component.css']
})
export class ViewMatchComponent implements OnInit, OnDestroy {

  constructor(
    private activatedRoute: ActivatedRoute,
    private http: HttpClient,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.subscription = this.activatedRoute.paramMap.subscribe(paramMap => {
      this.matchID = paramMap.get('matchID');
      this.matchInfo = {};
      this.oddsData = [];
      this.lastOddsDataID = undefined;
      this.lineChartDSCollection = {};
      this.tableDSCollection = {};
      if (this.matchID === null) {
        alert('Invalid matchID');
      } else {
        let cachedMatchID = sessionStorage.getItem("cachedMatchID");
        if (cachedMatchID === this.matchID) {
          let cachedMatchInfo = sessionStorage.getItem("cachedMatchInfo");
          if (cachedMatchInfo !== null) {
            this.matchInfo = JSON.parse(cachedMatchInfo);
          }
          let cachedOddsData = sessionStorage.getItem("cachedOddsData");
          if (cachedOddsData !== null) {
            this.oddsData = JSON.parse(cachedOddsData);
            this.lastOddsDataID = this.oddsData[this.oddsData.length - 1]['id'];
          }
        }
        sessionStorage.clear();
        this.getOddsData();
      }
    });
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  @ViewChild(MatAccordion) accordion!: MatAccordion;
  public showLineCharts: boolean = this.loadSetting('sLCs') === 'true';
  public readonly hideUnchangedRecords: boolean = true;

  public loadSetting(sKey: string): string | null {
    return localStorage.getItem(sKey);
  }
  public saveSetting(sKey: string, sVal: string): void {
    localStorage.setItem(sKey, sVal);
  }

  private subscription!: Subscription;
  public finishInit: boolean = false;

  public matchID: string | null = "";
  public matchInfo: { [key: string]: any } = {};
  public oddsData: { [key: string]: any }[] = [];
  public lastOddsDataID: number | undefined = undefined;

  public lineChartDSCollection: { [key: string]: any } = {};
  public tableDSCollection: { [key: string]: any } = {};

  public getOddsData(): void {
    this.http.get<{ [key: string]: any }>(environment.apiUrl + "/active-match/" + this.matchID + (this.lastOddsDataID ? "/" + this.lastOddsDataID : "")).subscribe({
      next: res => {
        if (res.hasOwnProperty('matchInfo')) {
          this.matchInfo = res['matchInfo'];
        }
        this.oddsData = (typeof this.lastOddsDataID === 'number') ? this.oddsData.concat(res["oddsData"]) : res["oddsData"];
        this.lastOddsDataID = this.oddsData[this.oddsData.length - 1]['id'];

        sessionStorage.setItem("cachedMatchID", this.matchID!);
        sessionStorage.setItem("cachedMatchInfo", JSON.stringify(this.matchInfo));
        sessionStorage.setItem("cachedOddsData", JSON.stringify(this.oddsData));
        this.parseRecords();
      },
      error: () => {
        this.router.navigate(["/page-not-found"]);
      }
    });
  }

  public parseRecords(): void {
    this.lineChartDSCollection = {};
    this.tableDSCollection = {};
    this.definedPools = this.availablePools.filter(e1 => Object.keys(this.oddsData[this.oddsData.length - 1]).includes(e1));
    this.definedPools.forEach(e1 => {
      this.lineChartDSCollection[e1] = [];
      if (['hilodds', 'fhlodds', 'chlodds'].includes(e1)) {
        this.tableDSCollection[e1] = this.parseMultiLineData(e1, this.DSP[e1]['requiredProperties'], this.DSP[e1]['colLabels'])
      } else {
        this.tableDSCollection[e1] = this.parseSimpleData(e1, this.DSP[e1]['requiredProperties'], this.DSP[e1]['colLabels'])
      }
    })
    this.finishInit = true;
  }

  public readonly oddsLabels: { [key: string]: any } = {
    hadodds: '主客和',
    fhaodds: '半場主客和',
    hhaodds: '讓球主客和',
    hdcodds: '讓球',
    hilodds: '入球大細',
    fhlodds: '半場入球大細',
    chlodds: '角球大細',
    //crsodds: '波膽',
    //fcsodds: '半場波膽',
    ftsodds: '第一隊入球',
    ttgodds: '總入球',
    ooeodds: '入球單雙',
    //fgsodds: '首名入球',
    hftodds: '半全場',
    //spcodds: '特別項目'
  }
  public availablePools = Object.keys(this.oddsLabels);
  public definedPools: string[] = [];

  public readonly DSP: { [key: string]: any } = {
    hadodds: {
      requiredProperties: {
        returnAsString: [],
        returnAsFloat: ['H', 'D', 'A']
      },
      colLabels: { H: '主', D: '和', A: '客' }
    },
    fhaodds: {
      requiredProperties: {
        returnAsString: [],
        returnAsFloat: ['H', 'D', 'A']
      },
      colLabels: { H: '主', D: '和', A: '客' }
    },
    hhaodds: {
      requiredProperties: {
        returnAsString: ['HG', 'AG'],
        returnAsFloat: ['H', 'D', 'A']
      },
      colLabels: { HG: '讓球', H: '主', D: '和', AG: '讓球', A: '客' }
    },
    hdcodds: {
      requiredProperties: {
        returnAsString: ['HG', 'AG'],
        returnAsFloat: ['H', 'A']
      },
      colLabels: { HG: '讓球', H: '主', AG: '讓球', A: '客' }
    },
    hilodds: {
      requiredProperties: {
        returnAsString: ['LINE'],
        returnAsFloat: ['H', 'L']
      },
      colLabels: { LINE: '球數', H: '大', L: '小' }
    },
    fhlodds: {
      requiredProperties: {
        returnAsString: ['LINE'],
        returnAsFloat: ['H', 'L']
      },
      colLabels: { LINE: '球數', H: '大', L: '小' }
    },
    chlodds: {
      requiredProperties: {
        returnAsString: ['LINE'],
        returnAsFloat: ['H', 'L']
      },
      colLabels: { LINE: '角球數', H: '大', L: '小' }
    },
    /*crsodds: {
      requiredProperties: {
        returnAsString: [],
        returnAsFloat: [
          'S0000', 'S0001', 'S0002', 'S0003', 'S0004', 'S0005',
          'S0100', 'S0101', 'S0102', 'S0103', 'S0104', 'S0105',
          'S0200', 'S0201', 'S0202', 'S0203', 'S0204', 'S0205',
          'S0300', 'S0301', 'S0302', 'S0303',
          'S0400', 'S0401', 'S0402',
          'S0500', 'S0501', 'S0502',
          'SM1MA', 'SM1MD', 'SM1MH'
        ]
      },
      colLabels: {}
    },
    fcsodds: {
      requiredProperties: {
        returnAsString: [],
        returnAsFloat: [
          'S0000', 'S0001', 'S0002', 'S0003', 'S0004', 'S0005',
          'S0100', 'S0101', 'S0102', 'S0103', 'S0104', 'S0105',
          'S0200', 'S0201', 'S0202', 'S0203', 'S0204', 'S0205',
          'S0300', 'S0301', 'S0302', 'S0303',
          'S0400', 'S0401', 'S0402',
          'S0500', 'S0501', 'S0502',
          'SM1MA', 'SM1MD', 'SM1MH'
        ]
      },
      colLabels: {}
    },*/
    ftsodds: {
      requiredProperties: {
        returnAsString: [],
        returnAsFloat: ['H', 'N', 'A']
      },
      colLabels: { H: '主', N: '無', A: '客' }
    },
    ttgodds: {
      requiredProperties: {
        returnAsString: [],
        returnAsFloat: ['P0', 'P1', 'P2', 'P3', 'P4', 'P5', 'P6', 'M7']
      },
      colLabels: { P0: '0', P1: '1', P2: '2', P3: '3', P4: '4', P5: '5', P6: '6', M7: '>=7' }
    },
    ooeodds: {
      requiredProperties: {
        returnAsString: [],
        returnAsFloat: ['O', 'E']
      },
      colLabels: { O: '單', E: '雙' }
    },
    /*fgsodds: {
      requiredProperties: {
        returnAsString: [],
        returnAsFloat: []
      },
      colLabels: {}
    },*/
    hftodds: {
      requiredProperties: {
        returnAsString: [],
        returnAsFloat: ['HH', 'HD', 'HA', 'DH', 'DD', 'DA', 'AH', 'AD', 'AA']
      },
      colLabels: { HH: '主主', HD: '主和', HA: '主客', DH: '和主', DD: '和和', DA: '和客', AH: '客主', AD: '客和', AA: '客客' }
    },
    /*spcodds: {
      requiredProperties: {
        returnAsString: [],
        returnAsFloat: []
      },
      colLabels: {}
    }*/
  };

  public parseSimpleData(
    oddsType: string,
    requiredProperties: {
      returnAsString: string[],
      returnAsFloat: string[]
    },
    colLabels: { [key: string]: string }
  ): { [key: string]: any } {
    let rPs = requiredProperties.returnAsString.concat(requiredProperties.returnAsFloat);
    if (rPs.length !== [...new Set(rPs)].length) {
      // Cannot returnAsString && returnAsFloat at same time
      console.log('Invalid requiredProperties');
      return {};
    }

    let dS = this.oddsData.filter(e1 => e1.hasOwnProperty(oddsType)).map(e1 => {
      let rawData: { [key: string]: any } = e1[oddsType];
      let returnVal: { [key: string]: any } = { createdAt: e1['updated_at'] };
      requiredProperties.returnAsString.forEach(e2 => {
        returnVal[e2] = rawData[e2];
      })
      requiredProperties.returnAsFloat.forEach((e2, i) => {
        let parsedNumber = parseFloat(rawData[e2].split('@')[1]);
        returnVal[e2] = parsedNumber;
        if (this.lineChartDSCollection[oddsType][i]) {
          this.lineChartDSCollection[oddsType][i]['series'].push({ name: e1['updated_at'], value: parsedNumber });
        } else {
          this.lineChartDSCollection[oddsType][i] = { name: colLabels[e2], series: [{ name: e1['updated_at'], value: parsedNumber }] };
        }
      })
      return returnVal;
    });
    this.lineChartDSCollection[oddsType].forEach((e1: { [key: string]: any }, i: number) => {
      this.lineChartDSCollection[oddsType][i]['series'] = this.lineChartDSCollection[oddsType][i]['series'].slice(-50);
    })
    return {
      dataSource: this.hideUnchangedRecords ? this.filterUnchangedRecords(dS) : dS,
      colLabels: { ...colLabels, createdAt: 'Date' },
      displayedColumns: ["createdAt"].concat((Object.keys(colLabels).length > 0) ? Object.keys(colLabels) : rPs)
    };
  }

  public parseMultiLineData(
    oddsType: string,
    requiredProperties: {
      returnAsString: string[],
      returnAsFloat: string[]
    },
    colLabels: { [key: string]: string }
  ): { [key: string]: any } {
    let rPs = requiredProperties.returnAsString.concat(requiredProperties.returnAsFloat);
    if (rPs.length !== [...new Set(rPs)].length) {
      // Cannot returnAsString && returnAsFloat at same time
      console.log('Invalid requiredProperties');
      return {};
    }

    let cL: { [key: string]: string } = {};
    let dC: string[] = ["createdAt"];
    let dS = this.oddsData.filter(e1 => e1.hasOwnProperty(oddsType)).map(e1 => {
      let rawData: { [key: string]: any }[] = e1[oddsType]['LINELIST'];
      let returnVal: { [key: string]: any } = { createdAt: e1['updated_at'] };
      rawData.forEach((e2, i2) => {
        requiredProperties.returnAsString.forEach(e3 => {
          returnVal[e3 + '_' + i2] = e2[e3];
          cL[e3 + '_' + i2] = colLabels[e3];
          if (dC.includes(e3 + '_' + i2) === false) {
            dC.push(e3 + '_' + i2);
          }
        })
        requiredProperties.returnAsFloat.forEach((e3, i3) => {
          let parsedNumber = parseFloat(e2[e3].split('@')[1]);
          returnVal[e3 + '_' + i2] = parsedNumber;
          cL[e3 + '_' + i2] = colLabels[e3];
          if (dC.includes(e3 + '_' + i2) === false) {
            dC.push(e3 + '_' + i2);
          }
          if (this.lineChartDSCollection[oddsType][i2 * 2 + i3]) {
            this.lineChartDSCollection[oddsType][i2 * 2 + i3]['series'].push({ name: e1['updated_at'], value: parsedNumber });
          } else {
            this.lineChartDSCollection[oddsType][i2 * 2 + i3] = { name: e2['LINE'] + ' ' + colLabels[e3], series: [{ name: e1['updated_at'], value: parsedNumber }] };
          }
        })
      })
      return returnVal;
    });
    this.lineChartDSCollection[oddsType].forEach((e1: { [key: string]: any }, i: number) => {
      this.lineChartDSCollection[oddsType][i]['series'] = this.lineChartDSCollection[oddsType][i]['series'].slice(-50);
    })
    return {
      dataSource: this.hideUnchangedRecords ? this.filterUnchangedRecords(dS) : dS,
      colLabels: { ...cL, createdAt: 'Date' },
      displayedColumns: dC
    };
  }

  public filterUnchangedRecords(rawData: { [key: string]: any }[]): { [key: string]: any }[] {
    return rawData.filter((e1, i) => {
      if (i === 0) { return true; }
      let temp1 = JSON.parse(JSON.stringify(e1));
      delete (temp1['createdAt']);
      let temp2 = JSON.parse(JSON.stringify(rawData[i - 1]));
      delete (temp2['createdAt']);
      return Object.keys(temp1).some(e2 => temp1[e2] !== temp2[e2]);
    })
  }

}
