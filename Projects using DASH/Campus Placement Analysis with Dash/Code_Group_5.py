# -*- coding: utf-8 -*-
import dash
import dash_core_components as dcc
import dash_html_components as html
import plotly.graph_objects as go
import plotly.express as px
import pandas as pd
import numpy as np
from sklearn.preprocessing import LabelEncoder
df=pd.read_csv("adult.csv")
df=df.replace({' ?':None})
df=df.dropna()
df.columns=['Age','Sectors','ID','Education','Education-Level','Martial-Status','Occupation','Relationship',
    'Race','Gender','Capital_Gain','Capital_Loss','HoursPerWeek','Native_Country', 'Income_Level']


ed_cols_ordered = [' Preschool',' 1st-4th',' 5th-6th',' 7th-8th',' 9th',' 10th',' 11th',' 12th',' HS-grad',
    ' Assoc-acdm',' Assoc-voc',' Prof-school',' Some-college',' Bachelors',' Masters',' Doctorate']

# External CSS Stylesheet
external_stylesheets = ['https://codepen.io/chriddyp/pen/bWLwgP.css']

app = dash.Dash(__name__, external_stylesheets=external_stylesheets)
sectors_list = df['Sectors'].dropna().unique()

df.loc[(df['Relationship'].str.strip() == 'Wife') | (df['Relationship'].str.strip() == 'Husband') ,'Relationship'] = 'Married-civ-Spouse'


# Dash layout
app.layout = html.Div([
# For the Main Title of Chart
    html.H2(children='Adult Earning',style={
            'textAlign': 'center',
            'color': '#000080'
        }
    ),
    #  Drop Down Boxes and its styles
    html.Div([
        html.Div([
            dcc.Dropdown(
                id='sectors_explored',
                options=[{'label': i, 'value': i} for i in sectors_list],
                placeholder="Select a Sector"
            )],style={'width': '20%','display': 'inline-block', 'padding': '20 20'}
        ),
        html.Div([
            dcc.Dropdown(id='Occupation_list', placeholder="Select a Occupation")
        ],style={'width': '20%', 'display': 'inline-block','padding': '20 20'}
        ),
    ],style={'display':'flex', 'justify-content':'center'}
    ),

    # Chart and its Style
    html.Div([

        html.Div([
        # For Bar Chart
            html.Div([
            html.H5(children='Working hours based on Country',style={
                'textAlign': 'center',
                'color': '#000080'
            }),
            # Radio button
            html.Div([
                dcc.RadioItems(
                    id='min_max_filter',
                    options=[{'label': i, 'value': i} for i in ['Maximum Hours', 'Minimum Hours']],
                    value='Maximum Hours',
                    labelStyle={'display': 'inline-block'}
                )
            ],
            style={'width': '100%', 'display': 'inline-block', 'textAlign': 'center'}),
            html.Div([
                dcc.Graph(id='crossfilter-bar')
            ])
            ],style={'display':'flex','flex-direction':'column','flex-grow':'1', 'margin':'5vh 2vh'}
        ),
        # For World Map Chart
        html.Div([
            html.H5(children='Occupation and Countries',style={
                    'textAlign': 'center',
                    'color': '#000080'
                }),
            html.Div([
                dcc.Graph(id='crossfilter-source-country')
            ]),
            ],style={'display':'flex', 'flex-direction':'column', 'flex-grow':'1','margin':'5vh 2vh'}
        ),
        ],style={'display':'flex', 'justify-content':'space-around', 'margin':'5vh 5vh'}
    )
    ]),
    html.Div([

        html.Div([
        # For Pie Chart
            html.Div([
            html.H5(children='Percentage of Income group',style={
                'textAlign': 'center',
                'color': '#000080'
            }),

            html.Div([
                dcc.Graph(id='Income_pie_chart')
            ])
            ],style={'display':'flex','flex-direction':'column','flex-grow':'1', 'margin':'5vh 2vh'}
        ),
        # For World Map Chart
        html.Div([
            html.H5(children='Working hours and age',style={
                    'textAlign': 'center',
                    'color': '#000080'
                }),
            html.Div([
                dcc.Graph(id='line_chart')
            ]),
            ],style={'display':'flex', 'flex-direction':'column', 'flex-grow':'1','margin':'5vh 2vh'}
        ),
        ],style={'display':'flex', 'justify-content':'space-around', 'margin':'5vh 5vh'}
    )
    ]),
    html.Div([
        html.Div([
            html.H5(children='Education and Working hours',style={
                    'textAlign': 'center',
                    'color': '#000080'
                }),
            html.Div([
                dcc.Graph(id='edu_income_chart')
            ]),
            ],style={'display':'flex', 'flex-direction':'column', 'flex-grow':'1','margin':'5vh 2vh'}
        ),
        ],style={'display':'flex', 'justify-content':'space-around', 'margin':'5vh 5vh'}
    ),html.Div([

        html.Div([
        # For Pie Chart
            html.Div([
            html.H5(children='Work Hours based on Gender and education',style={
                'textAlign': 'center',
                'color': '#000080'
            }),

            html.Div([
                dcc.Graph(id='WorkHours_hor_bar')
            ])
            ],style={'display':'flex','flex-direction':'column','flex-grow':'1', 'margin':'5vh 2vh'}
        ),
        # For World Map Chart
        html.Div([
            html.H5(children='Relationship over Age in this occupation',style={
                    'textAlign': 'center',
                    'color': '#000080'
                }),
            html.Div([
                dcc.Graph(id='box_chart')
            ]),
            ],style={'display':'flex', 'flex-direction':'column', 'flex-grow':'1','margin':'5vh 2vh'}
        ),
        ],style={'display':'flex', 'justify-content':'space-around', 'margin':'5vh 5vh'}
    )
    ]),
    html.Div([
        html.Div([
            html.H5(children='Relationship between Earning, Working Hours, Martial status, Education and Age ',style={
                    'textAlign': 'center',
                    'color': '#000080'
                }),
            html.Div([
                dcc.Graph(id='bubble_chart')
            ]),
            ],style={'display':'flex', 'flex-direction':'column', 'flex-grow':'1','margin':'5vh 2vh'}
        ),
        ],style={'display':'flex', 'justify-content':'space-around', 'margin':'5vh 5vh'}
    )
])

# Used for updating the bubble chart based on Drop Down Selection
@app.callback(
    dash.dependencies.Output('bubble_chart', 'figure'),
    [dash.dependencies.Input('sectors_explored', 'value'),
    dash.dependencies.Input('Occupation_list', 'value')])
def update_line_graph(current_sector,chosen_occupation):
    dff = df[df['Sectors'] == current_sector][df['Occupation'] == chosen_occupation]
    lb_make = LabelEncoder()
    dff.sort_values(by=['Education'], ascending=False, inplace=True)
    dff["Education_coded"] = lb_make.fit_transform(dff["Education"])
    if(dff.count()[0]>1):
        bubble_chart= px.scatter(dff, x="Age", y="HoursPerWeek", size="Education_coded", color="Income_Level",hover_name="Martial-Status", log_x=True, size_max=20)
    else:
        bubble_chart= px.scatter(dff, x="Age", y="HoursPerWeek")
    return bubble_chart



# Used for updating the box chart based on Drop Down Selection
@app.callback(
    dash.dependencies.Output('box_chart', 'figure'),
    [dash.dependencies.Input('sectors_explored', 'value'),
    dash.dependencies.Input('Occupation_list', 'value')])
def update_line_graph(current_sector,chosen_occupation):
    dff = df[df['Sectors'] == current_sector][df['Occupation'] == chosen_occupation]
    if(dff.count()[0]>1):
        box_chart=px.box(dff, x="Relationship", y="Age", color="Gender")
    else:
        box_chart=px.box(dff, x="Relationship", y="Age")
    box_chart.update_traces(quartilemethod="exclusive")
    return box_chart

# Used for updating the horizontal bar chart based on Drop Down Selection
@app.callback(
    dash.dependencies.Output('WorkHours_hor_bar', 'figure'),
    [dash.dependencies.Input('sectors_explored', 'value'),
    dash.dependencies.Input('Occupation_list', 'value')])
def update_hor_bar_graph(current_sector,chosen_occupation):
    dff = df[df['Sectors'] == current_sector][df['Occupation'] == chosen_occupation]
    hor_bar_chart=go.Figure()
    for Gender in dff['Gender'].unique():
        line_data=dff[dff['Gender']==Gender]
        line_data=line_data.loc[line_data.groupby(['Education'])['HoursPerWeek'].idxmax()]
        line_data.sort_values(by=['Education'], inplace=True)
        hor_bar_chart.add_trace(go.Bar(y=line_data['Education'], x=line_data['HoursPerWeek'],
                    orientation='h',
                    name=Gender))
    hor_bar_chart.update_layout(
        barmode='group',
        xaxis=dict(categoryorder="array", categoryarray=ed_cols_ordered))
    return hor_bar_chart

# Used for updating the bar chart based on Drop Down Selection
@app.callback(
    dash.dependencies.Output('crossfilter-bar', 'figure'),
    [dash.dependencies.Input('sectors_explored', 'value'),
    dash.dependencies.Input('Occupation_list', 'value'),
    dash.dependencies.Input('min_max_filter', 'value')])
def update_bar_graph(current_sector,chosen_occupation,min_max):
    dff = df[df['Sectors'] == current_sector][df['Occupation'] == chosen_occupation]
    if min_max == 'Minimum Hours':
        dff=dff.loc[dff.groupby(['Native_Country'])['HoursPerWeek'].idxmin()]
    else:
        dff=dff.loc[dff.groupby(['Native_Country'])['HoursPerWeek'].idxmax()]
    return px.bar(dff,x='Native_Country', y='HoursPerWeek')

# Used for updating the pie chart based on Drop Down Selection
@app.callback(
    dash.dependencies.Output('Income_pie_chart', 'figure'),
    [dash.dependencies.Input('sectors_explored', 'value'),
    dash.dependencies.Input('Occupation_list', 'value')])
def update_pie_graph(current_sector,chosen_occupation):
    dff = df[df['Sectors'] == current_sector][df['Occupation'] == chosen_occupation]
    labels=['<=50K','>50K']
    values=[dff[dff['Income_Level']==' <=50K']['Income_Level'].count(),dff[dff['Income_Level']==' >50K']['Income_Level'].count()]

    donutfig=go.Figure(data=go.Pie(
    name='Income ',
    labels=labels,
    values=values,
    hole=.4,
    hoverinfo="name+label+value"))

    return donutfig

# Used for updating the line chart based on Drop Down Selection
@app.callback(
    dash.dependencies.Output('line_chart', 'figure'),
    [dash.dependencies.Input('sectors_explored', 'value'),
    dash.dependencies.Input('Occupation_list', 'value')])
def update_line_graph(current_sector,chosen_occupation):
    dff = df[df['Sectors'] == current_sector][df['Occupation'] == chosen_occupation]
    linechart=go.Figure()
    for line in dff['Gender'].unique():
        line_data=dff[dff['Gender']==line]
        line_data=line_data.loc[line_data.groupby(['Age'])['HoursPerWeek'].idxmax()]
        line_data.sort_values(by=['Age'], inplace=True)
        linechart.add_trace(go.Scatter(x=line_data['Age'], y=line_data['HoursPerWeek'],
                    mode='lines+markers',
                    name=line))
    return linechart

# Used for updating the Company Drop Down box based on Country Drop Down Selection
@app.callback(
    dash.dependencies.Output('Occupation_list', 'options'),
    [dash.dependencies.Input('sectors_explored', 'value')])
def update_class_list(current_sector):
    dff = df[df['Sectors'] == current_sector]
    return [{'label': i, 'value': i} for i in dff['Occupation'].unique()]

# Used for updating the world map chart based on Drop Down Selection
@app.callback(
    dash.dependencies.Output('crossfilter-source-country', 'figure'),
    [dash.dependencies.Input('sectors_explored', 'value'),
    dash.dependencies.Input('Occupation_list', 'value')])
def update_map(current_sector, chosen_occupation):

    dff = df[df['Sectors'] == current_sector][df['Occupation'] == chosen_occupation]

    mapFig = go.Figure(data=go.Choropleth(
        locations = dff['Native_Country'].dropna(),
        locationmode = 'country names',
        z = dff['Education-Level'],
        text = 'Income_Level: '+dff['Income_Level'],
        colorscale = 'YlGnBu',
        marker_line_color='darkgray',
        marker_line_width=0.5,
        colorbar_tickprefix = '',
        colorbar_title = 'Education-Level'
    ))

    mapFig.update_layout(
        title_text='Sources of Cocoa for ' + str(chosen_occupation) + '<br/>',
        geo=dict(
            showframe=False,
            showcoastlines=False,
            projection_type='equirectangular'
        ),margin={"r":0,"t":0,"l":0,"b":0}
    )

    return mapFig

@app.callback(
    dash.dependencies.Output('edu_income_chart', 'figure'),
    [dash.dependencies.Input('sectors_explored', 'value'),
    dash.dependencies.Input('Occupation_list', 'value')])
def update_category_graph(current_sector,chosen_occupation):
    dff = df[df['Sectors'] == current_sector][df['Occupation'] == chosen_occupation]
    ed_cols = dff['Education']

    fig = go.Figure()

    fig.add_trace(go.Box(
        y=dff['Age'],
        x=ed_cols,
        name='Age',
        marker_color='#3D9970'
    ))

    fig.add_trace(go.Box(
        y=dff['HoursPerWeek'],
        x=ed_cols,
        name='Hours Per Week',
        marker_color='#3F3920'
    ))

    fig.update_layout(
        xaxis=dict(title='Education Level', zeroline=False, categoryorder="array", categoryarray=ed_cols_ordered),
        boxmode='group'
    )

    return fig

# Main Function
if __name__ == '__main__':
    app.run_server(debug=True)
